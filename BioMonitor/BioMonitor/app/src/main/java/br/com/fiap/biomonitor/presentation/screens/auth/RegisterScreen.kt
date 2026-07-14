package br.com.fiap.biomonitor.presentation.screens.auth

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.fiap.biomonitor.presentation.components.BioButton
import br.com.fiap.biomonitor.presentation.components.BioPasswordField
import br.com.fiap.biomonitor.presentation.components.BioTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(state.registerSuccess) {
        if (state.registerSuccess) {
            onRegisterSuccess()
            viewModel.resetRegisterSuccess()
        }
    }


    LaunchedEffect(state.registerError) {
        state.registerError?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearRegisterError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Conta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
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
                Text(
                    text = "Junte-se à comunidade BioMonitor",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))


                BioTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    label = "Nome completo",
                    placeholder = "Seu nome",
                    leadingIcon = Icons.Default.Person,
                    isError = state.nameError != null,
                    errorMessage = state.nameError,
                    imeAction = ImeAction.Next,
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))


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


                BioTextField(
                    value = state.city,
                    onValueChange = viewModel::onCityChange,
                    label = "Cidade",
                    placeholder = "Sua cidade",
                    leadingIcon = Icons.Default.LocationCity,
                    isError = state.cityError != null,
                    errorMessage = state.cityError,
                    imeAction = ImeAction.Next,
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))


                BioPasswordField(
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Senha",
                    placeholder = "Mínimo 6 caracteres",
                    leadingIcon = Icons.Default.Lock,
                    isError = state.passwordError != null,
                    errorMessage = state.passwordError,
                    imeAction = ImeAction.Next,
                    enabled = !state.isLoading
                )


                if (state.password.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordStrengthIndicator(strength = state.passwordStrength)
                }

                Spacer(modifier = Modifier.height(16.dp))


                BioPasswordField(
                    value = state.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = "Confirmar senha",
                    placeholder = "Repita a senha",
                    leadingIcon = Icons.Default.Lock,
                    isError = state.confirmPasswordError != null,
                    errorMessage = state.confirmPasswordError,
                    imeAction = ImeAction.Done,
                    onImeAction = { viewModel.register() },
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.termsAccepted,
                        onCheckedChange = viewModel::onTermsAcceptedChange,
                        enabled = !state.isLoading,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Text(
                        text = "Li e aceito os Termos de Uso e Política de Privacidade",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (state.termsError != null)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (state.termsError != null) {
                    Text(
                        text = state.termsError!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))


                BioButton(
                    text = "Criar Conta",
                    onClick = { viewModel.register() },
                    isLoading = state.isLoading,
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun PasswordStrengthIndicator(
    strength: RegisterViewModel.PasswordStrength,
    modifier: Modifier = Modifier
) {
    val (label, color, progress) = when (strength) {
        RegisterViewModel.PasswordStrength.NONE -> Triple("", Color.Transparent, 0f)
        RegisterViewModel.PasswordStrength.WEAK -> Triple("Fraca", Color(0xFFE53935), 0.33f)
        RegisterViewModel.PasswordStrength.MEDIUM -> Triple("Média", Color(0xFFFFA726), 0.66f)
        RegisterViewModel.PasswordStrength.STRONG -> Triple("Forte", Color(0xFF43A047), 1f)
    }

    val animatedColor by animateColorAsState(
        targetValue = color,
        label = "strengthColor"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Força da senha:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = animatedColor
            )
        }

        Spacer(modifier = Modifier.height(4.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(3) { index ->
                val isActive = when (strength) {
                    RegisterViewModel.PasswordStrength.NONE -> false
                    RegisterViewModel.PasswordStrength.WEAK -> index == 0
                    RegisterViewModel.PasswordStrength.MEDIUM -> index <= 1
                    RegisterViewModel.PasswordStrength.STRONG -> true
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (isActive) animatedColor
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            }
        }
    }
}
