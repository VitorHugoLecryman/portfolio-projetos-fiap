package br.com.fiap.inovagab.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.fiap.inovagab.databinding.ActivityLeaderHomeBinding
import br.com.fiap.inovagab.viewmodel.LeaderViewModel
import br.com.fiap.inovagab.viewmodel.LoginViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class LeaderHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeaderHomeBinding
    private val viewModel: LeaderViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    private var userId: String = ""
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("USER_ID") ?: ""
        userName = intent.getStringExtra("USER_NAME") ?: ""

        setupUI()
        setupObservers()
        setupListeners()
        carregarDados()
    }

    private fun setupUI() {
        binding.tvNomeLider.text = "Olá, $userName"
    }

    private fun setupObservers() {
        viewModel.totalProjetos.observe(this) { total ->
            binding.tvTotalProjetos.text = total.toString()
        }

        viewModel.projetosAtivos.observe(this) { ativos ->
            binding.tvProjetosAtivos.text = ativos.toString()
        }

        viewModel.projetosConcluidos.observe(this) { concluidos ->
            binding.tvProjetosConcluidos.text = concluidos.toString()
        }

        viewModel.investimentoTotal.observe(this) { investimento ->
            binding.tvInvestimentoTotal.text = "R$ %.2f".format(investimento)
        }

        viewModel.roiGeral.observe(this) { roi ->
            binding.tvRoi.text = "%.2f%%".format(roi)
        }

        viewModel.retornoTotal.observe(this) { retorno ->
            binding.tvRetornoTotal.text = "R$ %.2f".format(retorno)
        }

        viewModel.distribuicaoPorEtapa.observe(this) { distribuicao ->
            val entries = distribuicao
                .filterValues { it > 0 }
                .map { (etapa, quantidade) -> PieEntry(quantidade.toFloat(), etapa) }

            if (entries.isNotEmpty()) {
                val dataSet = PieDataSet(entries, "Projetos por Etapa")
                dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
                dataSet.valueTextSize = 14f

                val pieData = PieData(dataSet)
                binding.pieChart.data = pieData
                binding.pieChart.description.isEnabled = false
                binding.pieChart.centerText = "Projetos"
                binding.pieChart.animateY(1000)
                binding.pieChart.invalidate()
            } else {
                binding.pieChart.clear()
            }
        }

        viewModel.porEstrategia.observe(this) { estrategias ->
            if (estrategias.isEmpty()) {
                binding.cardEstrategia.visibility = View.GONE
                return@observe
            }
            binding.cardEstrategia.visibility = View.VISIBLE
            binding.tvPorEstrategia.text = estrategias.joinToString("\n\n") { estrategia ->
                "${estrategia.orientacaoTitulo}\n" +
                    "${estrategia.totalProjetos} projeto(s) · " +
                    "investimento R$ %.2f · retorno R$ %.2f · ROI %.1f%%".format(
                        estrategia.investimento, estrategia.retorno, estrategia.roi
                    )
            }
        }

        viewModel.insightsIa.observe(this) { insights ->
            insights?.let {
                val sugestoes = it.sugestoes.joinToString("\n") { sugestao -> "• $sugestao" }
                AlertDialog.Builder(this)
                    .setTitle("Insights da IA")
                    .setMessage("${it.analise}\n\n$sugestoes")
                    .setPositiveButton("Fechar", null)
                    .show()
                viewModel.limparInsightsIa()
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
        binding.btnOrientacoes.setOnClickListener {
            startActivity(Intent(this, OrientacoesManagerActivity::class.java).apply {
                putExtra("USER_ID", userId)
                putExtra("USER_NAME", userName)
            })
        }

        binding.btnVerProjetos.setOnClickListener {
            startActivity(Intent(this, ProjectsActivity::class.java).apply {
                putExtra("USER_ID", userId)
                putExtra("USER_NAME", userName)
                putExtra("IS_LEADER", true)
            })
        }

        binding.btnInsightsIa.setOnClickListener {
            viewModel.gerarInsightsIa()
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
        viewModel.carregarProjetos()
        viewModel.carregarPorEstrategia()
    }

    override fun onResume() {
        super.onResume()
        carregarDados()
    }
}
