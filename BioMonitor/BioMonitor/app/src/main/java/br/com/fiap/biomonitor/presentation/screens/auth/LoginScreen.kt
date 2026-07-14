package br.com.fiap.biomonitor.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.fiap.biomonitor.presentation.components.BioButton
import br.com.fiap.biomonitor.presentation.components.BioPasswordField
import br.com.fiap.biomonitor.presentation.components.BioTextField
import br.com.fiap.biomonitor.presentation.components.BioTextButton

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) {
            onLoginSuccess()
            viewModel.resetLoginSuccess()
        }
    }


    LaunchedEffect(state.loginError) {
        state.loginError?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearLoginError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))


                Icon(
                    imageVector = Icons.Filled.Eco,
                    contentDescription = "BioMonitor Logo",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "BioMonitor",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Faça login para continuar",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(48.dp))


                BioTextField(
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    label = "Email",
                    placeholder = "seu@email.com",
                    leadingIcon = Icons.Default.Email,
                    isError = state.emailError != null,
                    errorMessage = state.emailError,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))


                BioPasswordField(
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Senha",
                    placeholder = "Sua senha",
                    leadingIcon = Icons.Default.Lock,
                    isError = state.passwordError != null,
                    errorMessage = state.passwordError,
                    imeAction = ImeAction.Done,
                    onImeAction = { viewModel.login() },
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(32.dp))


                BioButton(
                    text = "Entrar",
                    onClick = { viewModel.login() },
                    isLoading = state.isLoading,
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Não tem uma conta?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    BioTextButton(
                        text = "Cadastre-se",
                        onClick = onNavigateToRegister,
                        enabled = !state.isLoading
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))


                Text(
                    text = "Ao entrar, você concorda com nossos\nTermos de Uso e Política de Privacidade",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
