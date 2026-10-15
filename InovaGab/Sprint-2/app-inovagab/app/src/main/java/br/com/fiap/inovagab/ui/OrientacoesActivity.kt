package br.com.fiap.inovagab.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.inovagab.databinding.ActivityOrientacoesBinding
import br.com.fiap.inovagab.ui.adapter.OrientacaoAdapter
import br.com.fiap.inovagab.viewmodel.LeaderViewModel

class OrientacoesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrientacoesBinding
    private val viewModel: LeaderViewModel by viewModels()
    private lateinit var adapter: OrientacaoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrientacoesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObservers()
        viewModel.carregarOrientacoes()
    }

    private fun setupUI() {
        binding.toolbar.title = "Orientações Estratégicas"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        adapter = OrientacaoAdapter(emptyList(), isReadOnly = true)
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
    }
}
