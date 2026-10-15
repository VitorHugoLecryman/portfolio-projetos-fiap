package br.com.fiap.inovagab.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.inovagab.databinding.ActivityRankingBinding
import br.com.fiap.inovagab.ui.adapter.RankingAdapter
import br.com.fiap.inovagab.viewmodel.OperatorViewModel

class RankingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRankingBinding
    private val viewModel: OperatorViewModel by viewModels()
    private lateinit var rankingAdapter: RankingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObservers()
        viewModel.carregarRanking()
    }

    private fun setupUI() {
        binding.toolbar.title = "Ranking de Inovação"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        rankingAdapter = RankingAdapter(emptyList())
        binding.rvRanking.layoutManager = LinearLayoutManager(this)
        binding.rvRanking.adapter = rankingAdapter
    }

    private fun setupObservers() {
        viewModel.ranking.observe(this) { ranking ->
            rankingAdapter.atualizarLista(ranking)
        }

        viewModel.mensagem.observe(this) { msg ->
            msg?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.limparMensagem()
            }
        }
    }
}
