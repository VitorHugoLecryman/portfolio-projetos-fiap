package br.com.fiap.inovagab.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.inovagab.databinding.ActivityManagerHomeBinding
import br.com.fiap.inovagab.ui.adapter.IdeaCurationAdapter
import br.com.fiap.inovagab.viewmodel.LoginViewModel
import br.com.fiap.inovagab.viewmodel.ManagerViewModel

class ManagerHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagerHomeBinding
    private val viewModel: ManagerViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var curationAdapter: IdeaCurationAdapter

    private var userId: String = ""
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("USER_ID") ?: ""
        userName = intent.getStringExtra("USER_NAME") ?: ""

        setupUI()
        setupObservers()
        setupListeners()
        carregarDados()
    }

    private fun setupUI() {
        binding.tvNomeGestor.text = "Olá, $userName"

        curationAdapter = IdeaCurationAdapter(
            ideias = emptyList(),
            onAprovar = { ideia, impacto, esforco ->
                viewModel.aprovarIdeia(ideia.id, ideia.autorId, impacto, esforco)
            },
            onRejeitar = { ideia, motivo ->
                viewModel.rejeitarIdeia(ideia.id, motivo)
            },
            onAvaliarIa = { ideia ->
                viewModel.avaliarIdeiaComIa(ideia.id)
            }
        )
        binding.rvIdeiasCuradoria.layoutManager = LinearLayoutManager(this)
        binding.rvIdeiasCuradoria.adapter = curationAdapter
    }

    private fun setupObservers() {
        viewModel.ideiasEnviadas.observe(this) { ideias ->
            curationAdapter.atualizarLista(ideias)
            binding.tvSemIdeias.visibility = if (ideias.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.avaliacaoIa.observe(this) { avaliacao ->
            avaliacao?.let {
                AlertDialog.Builder(this)
                    .setTitle("Avaliação da IA: ${it.score}/100")
                    .setMessage(it.justificativa)
                    .setPositiveButton("Entendi", null)
                    .show()
                viewModel.limparAvaliacaoIa()
            }
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
        binding.btnProjetos.setOnClickListener {
            startActivity(Intent(this, ProjectsActivity::class.java).apply {
                putExtra("USER_ID", userId)
                putExtra("USER_NAME", userName)
            })
        }

        binding.btnOrientacoes.setOnClickListener {
            startActivity(Intent(this, OrientacoesActivity::class.java))
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
        viewModel.carregarIdeiasEnviadas()
    }

    override fun onResume() {
        super.onResume()
        carregarDados()
    }
}
