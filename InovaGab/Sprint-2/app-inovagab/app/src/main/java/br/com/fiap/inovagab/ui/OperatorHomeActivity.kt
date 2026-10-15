package br.com.fiap.inovagab.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.inovagab.R
import br.com.fiap.inovagab.databinding.ActivityOperatorHomeBinding
import br.com.fiap.inovagab.model.StrategicOrientation
import br.com.fiap.inovagab.ui.adapter.IdeaAdapter
import br.com.fiap.inovagab.viewmodel.LoginViewModel
import br.com.fiap.inovagab.viewmodel.OperatorViewModel

class OperatorHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOperatorHomeBinding
    private val viewModel: OperatorViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var ideaAdapter: IdeaAdapter

    private var userId: String = ""
    private var userName: String = ""
    private var userPoints: Int = 0
    private var orientacoesDisponiveis: List<StrategicOrientation> = emptyList()

    private val categorias = listOf(
        "Segurança", "Eficiência Operacional", "Redução de Custos",
        "Melhoria de Processo", "Tecnologia", "Sustentabilidade", "Outro"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperatorHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("USER_ID") ?: ""
        userName = intent.getStringExtra("USER_NAME") ?: ""
        userPoints = intent.getIntExtra("USER_POINTS", 0)

        setupUI()
        setupObservers()
        setupListeners()
        carregarDados()
    }

    private fun setupUI() {
        binding.tvNomeUsuario.text = "Olá, $userName"
        binding.tvPontos.text = "$userPoints pontos"

        ideaAdapter = IdeaAdapter(emptyList())
        binding.rvMinhasIdeias.layoutManager = LinearLayoutManager(this)
        binding.rvMinhasIdeias.adapter = ideaAdapter

        // Spinner de categorias
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategoria.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.minhasIdeias.observe(this) { ideias ->
            ideaAdapter.atualizarLista(ideias)
            binding.tvSemIdeias.visibility = if (ideias.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.orientacoes.observe(this) { orientacoes ->
            orientacoesDisponiveis = orientacoes
            val titulos = if (orientacoes.isEmpty()) {
                listOf("Nenhuma orientação vigente")
            } else {
                orientacoes.map { it.titulo }
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titulos)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerOrientacao.adapter = adapter
            binding.btnEnviarIdeia.isEnabled = orientacoes.isNotEmpty()
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
        binding.btnEnviarIdeia.setOnClickListener {
            val titulo = binding.etTituloIdeia.text.toString().trim()
            val descricao = binding.etDescricaoIdeia.text.toString().trim()
            val categoria = binding.spinnerCategoria.selectedItem.toString()
            val posicao = binding.spinnerOrientacao.selectedItemPosition
            val orientacaoId = orientacoesDisponiveis.getOrNull(posicao)?.id ?: ""

            viewModel.enviarIdeia(titulo, descricao, categoria, orientacaoId, userId, userName)

            // Limpar campos após envio
            binding.etTituloIdeia.text?.clear()
            binding.etDescricaoIdeia.text?.clear()
        }

        binding.btnOrientacoes.setOnClickListener {
            startActivity(Intent(this, OrientacoesActivity::class.java))
        }

        binding.btnRanking.setOnClickListener {
            startActivity(Intent(this, RankingActivity::class.java).apply {
                putExtra("USER_ID", userId)
            })
        }

        binding.btnHistoricoPontos.setOnClickListener {
            startActivity(Intent(this, PointsHistoryActivity::class.java).apply {
                putExtra("USER_ID", userId)
            })
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sair") { _, _ ->
                    loginViewModel.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    private fun carregarDados() {
        viewModel.carregarMinhasIdeias(userId)
        viewModel.carregarOrientacoes()
    }

    override fun onResume() {
        super.onResume()
        carregarDados()
    }
}
