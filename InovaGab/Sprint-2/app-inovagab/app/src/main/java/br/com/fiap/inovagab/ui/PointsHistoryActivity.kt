package br.com.fiap.inovagab.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.inovagab.databinding.ActivityPointsHistoryBinding
import br.com.fiap.inovagab.ui.adapter.PointsHistoryAdapter
import br.com.fiap.inovagab.viewmodel.OperatorViewModel

class PointsHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPointsHistoryBinding
    private val viewModel: OperatorViewModel by viewModels()
    private lateinit var historyAdapter: PointsHistoryAdapter

    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointsHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("USER_ID") ?: ""

        setupUI()
        setupObservers()
        viewModel.carregarHistoricoPontos(userId)
    }

    private fun setupUI() {
        binding.toolbar.title = "Histórico de Pontos"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        historyAdapter = PointsHistoryAdapter(emptyList())
        binding.rvHistorico.layoutManager = LinearLayoutManager(this)
        binding.rvHistorico.adapter = historyAdapter
    }

    private fun setupObservers() {
        viewModel.historicoPontos.observe(this) { transacoes ->
            historyAdapter.atualizarLista(transacoes)
        }

        viewModel.mensagem.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.limparMensagem()
            }
        }
    }
}
