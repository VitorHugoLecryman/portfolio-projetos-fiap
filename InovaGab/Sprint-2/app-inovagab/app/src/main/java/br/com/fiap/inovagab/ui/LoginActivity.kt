package br.com.fiap.inovagab.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.com.fiap.inovagab.databinding.ActivityLoginBinding
import br.com.fiap.inovagab.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()

        // Verificar se já está logado
        viewModel.verificarSessao()
    }

    private fun setupObservers() {
        viewModel.usuario.observe(this) { user ->
            user?.let {
                val intent = when (it.perfil) {
                    "operador" -> Intent(this, OperatorHomeActivity::class.java)
                    "gestor" -> Intent(this, ManagerHomeActivity::class.java)
                    "lider" -> Intent(this, LeaderHomeActivity::class.java)
                    else -> null
                }
                intent?.let { i ->
                    i.putExtra("USER_ID", it.uid)
                    i.putExtra("USER_NAME", it.nome)
                    i.putExtra("USER_ROLE", it.perfil)
                    i.putExtra("USER_POINTS", it.pontos)
                    startActivity(i)
                    finish()
                }
            }
        }

        viewModel.erro.observe(this) { erro ->
            erro?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.limparErro()
            }
        }

        viewModel.carregando.observe(this) { carregando ->
            binding.progressBar.visibility = if (carregando) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !carregando
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val senha = binding.etSenha.text.toString().trim()
            viewModel.login(email, senha)
        }
    }
}
