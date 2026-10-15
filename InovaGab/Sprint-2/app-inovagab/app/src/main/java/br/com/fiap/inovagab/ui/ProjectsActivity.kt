package br.com.fiap.inovagab.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.inovagab.databinding.ActivityProjectsBinding
import br.com.fiap.inovagab.model.Project
import br.com.fiap.inovagab.ui.adapter.ProjectAdapter
import br.com.fiap.inovagab.viewmodel.ManagerViewModel

class ProjectsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProjectsBinding
    private val viewModel: ManagerViewModel by viewModels()
    private lateinit var projectAdapter: ProjectAdapter

    private var userId: String = ""
    private var userName: String = ""
    private var isLeader: Boolean = false

    private var origens: List<Origem> = emptyList()

    private data class Origem(
        val rotulo: String,
        val ideiaId: String?,
        val orientacaoId: String?
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("USER_ID") ?: ""
        userName = intent.getStringExtra("USER_NAME") ?: ""
        isLeader = intent.getBooleanExtra("IS_LEADER", false)

        setupUI()
        setupObservers()
        setupListeners()
        carregarDados()
    }

    private fun carregarDados() {
        viewModel.carregarProjetos()
        if (!isLeader) {
            viewModel.carregarTodasIdeias()
            viewModel.carregarOrientacoes()
        }
    }

    override fun onResume() {
        super.onResume()
        carregarDados()
    }

    private fun atualizarOrigens() {
        val ideias = viewModel.todasIdeias.value.orEmpty()
            .filter { it.status == "Aprovada" }
            .map { Origem("Ideia: ${it.titulo}", it.id, null) }

        val orientacoes = viewModel.orientacoes.value.orEmpty()
            .map { Origem("Orientação: ${it.titulo}", null, it.id) }

        origens = ideias + orientacoes
    }

    private fun setupUI() {
        binding.toolbar.title = "Projetos"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Esconder botão de criar projeto se for líder (somente visualização)
        if (isLeader) {
            binding.btnCriarProjeto.visibility = View.GONE
        }

        projectAdapter = ProjectAdapter(
            projetos = emptyList(),
            isReadOnly = isLeader,
            onAlterarEtapa = { projeto -> mostrarDialogEtapa(projeto) },
            onAdicionarResultados = { projeto -> mostrarDialogResultados(projeto) }
        )
        binding.rvProjetos.layoutManager = LinearLayoutManager(this)
        binding.rvProjetos.adapter = projectAdapter
    }

    private fun setupObservers() {
        viewModel.projetos.observe(this) { projetos ->
            projectAdapter.atualizarLista(projetos)
            binding.tvSemProjetos.visibility = if (projetos.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.todasIdeias.observe(this) { atualizarOrigens() }

        viewModel.orientacoes.observe(this) { atualizarOrigens() }

        viewModel.mensagem.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.limparMensagem()
            }
        }

        viewModel.carregando.observe(this) { carregando ->
            binding.progressBar.visibility = if (carregando) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        binding.btnCriarProjeto.setOnClickListener {
            mostrarDialogCriarProjeto()
        }
    }

    private fun mostrarDialogCriarProjeto() {
        if (origens.isEmpty()) {
            Toast.makeText(
                this,
                "Nenhuma ideia aprovada ou orientação vigente para vincular ao projeto",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 32, 48, 0)
        }
        val etTitulo = EditText(this).apply { hint = "Título do Projeto" }
        val etDescricao = EditText(this).apply { hint = "Descrição" }
        val etOrcamento = EditText(this).apply {
            hint = "Orçamento (R$)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        val etPrazo = EditText(this).apply { hint = "Prazo (ex: 3 meses)" }
        val spinnerOrigem = Spinner(this).apply {
            adapter = ArrayAdapter(
                this@ProjectsActivity,
                android.R.layout.simple_spinner_dropdown_item,
                origens.map { it.rotulo }
            )
        }

        layout.addView(etTitulo)
        layout.addView(etDescricao)
        layout.addView(etOrcamento)
        layout.addView(etPrazo)
        layout.addView(spinnerOrigem)

        AlertDialog.Builder(this)
            .setTitle("Novo Projeto")
            .setView(layout)
            .setPositiveButton("Criar") { _, _ ->
                val origem = origens.getOrNull(spinnerOrigem.selectedItemPosition)
                val project = Project(
                    titulo = etTitulo.text.toString().trim(),
                    descricao = etDescricao.text.toString().trim(),
                    ideiaId = origem?.ideiaId ?: "",
                    orientacaoId = origem?.orientacaoId ?: "",
                    orcamento = etOrcamento.text.toString().toDoubleOrNull() ?: 0.0,
                    prazo = etPrazo.text.toString().trim(),
                    gestorId = userId,
                    gestorNome = userName
                )
                viewModel.criarProjeto(project)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogEtapa(projeto: Project) {
        val etapas = when (projeto.etapa) {
            "Planejamento" -> arrayOf("Em Andamento", "Cancelado")
            "Em Andamento" -> arrayOf("Concluído", "Cancelado")
            else -> return
        }

        AlertDialog.Builder(this)
            .setTitle("Alterar Etapa")
            .setItems(etapas) { _, which ->
                viewModel.atualizarEtapaProjeto(projeto.id, etapas[which])
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogResultados(projeto: Project) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 32, 48, 0)
        }
        val etRetorno = EditText(this).apply {
            hint = "Retorno Financeiro (R$)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        val etProdutividade = EditText(this).apply {
            hint = "Ganho de Produtividade (%)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        val etQualitativos = EditText(this).apply { hint = "Resultados Qualitativos" }
        layout.addView(etRetorno)
        layout.addView(etProdutividade)
        layout.addView(etQualitativos)

        AlertDialog.Builder(this)
            .setTitle("Adicionar Resultados")
            .setView(layout)
            .setPositiveButton("Salvar") { _, _ ->
                viewModel.adicionarResultados(
                    projectId = projeto.id,
                    retornoFinanceiro = etRetorno.text.toString().toDoubleOrNull() ?: 0.0,
                    ganhoProdutividade = etProdutividade.text.toString().toDoubleOrNull() ?: 0.0,
                    resultadosQualitativos = etQualitativos.text.toString().trim()
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
