package br.com.fiap.inovagab.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.inovagab.databinding.ActivityOrientacoesManagerBinding
import br.com.fiap.inovagab.model.StrategicOrientation
import br.com.fiap.inovagab.ui.adapter.OrientacaoAdapter
import br.com.fiap.inovagab.viewmodel.LeaderViewModel
import com.google.android.material.textfield.TextInputEditText
import android.widget.LinearLayout

class OrientacoesManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrientacoesManagerBinding
    private val viewModel: LeaderViewModel by viewModels()
    private lateinit var adapter: OrientacaoAdapter

    private var userId: String = ""
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrientacoesManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("USER_ID") ?: ""
        userName = intent.getStringExtra("USER_NAME") ?: ""

        setupUI()
        setupObservers()
        setupListeners()
        viewModel.carregarOrientacoes()
    }

    private fun setupUI() {
        binding.toolbar.title = "Gerenciar Orientações"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        adapter = OrientacaoAdapter(
            orientacoes = emptyList(),
            isReadOnly = false,
            onEditar = { orientacao -> mostrarDialogEditar(orientacao) },
            onExcluir = { orientacao -> confirmarExclusao(orientacao) }
        )
        binding.rvOrientacoes.layoutManager = LinearLayoutManager(this)
        binding.rvOrientacoes.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.orientacoes.observe(this) { orientacoes ->
            adapter.atualizarLista(orientacoes)
            binding.tvSemOrientacoes.visibility = if (orientacoes.isEmpty()) View.VISIBLE else View.GONE
        }

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
        binding.btnCriarOrientacao.setOnClickListener {
            val titulo = binding.etTitulo.text.toString().trim()
            val descricao = binding.etDescricao.text.toString().trim()
            val categoria = binding.etCategoria.text.toString().trim()
            val campanha = binding.etCampanha.text.toString().trim()
            viewModel.criarOrientacao(titulo, descricao, categoria, campanha, userId, userName)
            binding.etTitulo.text?.clear()
            binding.etDescricao.text?.clear()
            binding.etCategoria.text?.clear()
            binding.etCampanha.text?.clear()
        }
    }

    private fun mostrarDialogEditar(orientacao: StrategicOrientation) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 32, 48, 0)
        }
        val etTitulo = TextInputEditText(this).apply {
            hint = "Título"
            setText(orientacao.titulo)
        }
        val etDescricao = TextInputEditText(this).apply {
            hint = "Descrição"
            setText(orientacao.descricao)
        }
        val etCategoria = TextInputEditText(this).apply {
            hint = "Categoria"
            setText(orientacao.categoria)
        }
        val etCampanha = TextInputEditText(this).apply {
            hint = "Campanha"
            setText(orientacao.campanha)
        }
        layout.addView(etTitulo)
        layout.addView(etDescricao)
        layout.addView(etCategoria)
        layout.addView(etCampanha)

        AlertDialog.Builder(this)
            .setTitle("Editar Orientação")
            .setView(layout)
            .setPositiveButton("Salvar") { _, _ ->
                viewModel.atualizarOrientacao(
                    orientacao.id,
                    etTitulo.text.toString().trim(),
                    etDescricao.text.toString().trim(),
                    etCategoria.text.toString().trim(),
                    etCampanha.text.toString().trim()
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmarExclusao(orientacao: StrategicOrientation) {
        AlertDialog.Builder(this)
            .setTitle("Excluir Orientação")
            .setMessage("Tem certeza que deseja excluir \"${orientacao.titulo}\"?")
            .setPositiveButton("Excluir") { _, _ ->
                viewModel.excluirOrientacao(orientacao.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
