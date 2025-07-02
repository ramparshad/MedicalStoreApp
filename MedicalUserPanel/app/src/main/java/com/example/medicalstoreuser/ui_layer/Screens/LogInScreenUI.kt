package com.example.medicalstoreuser.ui_layer.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jcmodule.GrowingDotsProgressBar
import com.example.medicalstoreuser.R
import com.example.medicalstoreuser.ui_layer.AppViewModel
import com.example.medicalstoreuser.ui_layer.common.MultiColorText
import com.example.medicalstoreuser.ui_layer.navigation.Routes

@Composable
fun LogInScreenUI(
    navController: NavController,
    viewModel: AppViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userEmail = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }
    val state = viewModel.loginResponse.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.size(20.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(150.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.size(20.dp))

            OutlinedTextField(
                value = userEmail.value,
                onValueChange = { userEmail.value = it },
                label = { Text("Email") },
                placeholder = { Text("Enter your email") }
            )
            Spacer(modifier = Modifier.size(20.dp))

            OutlinedTextField(
                value = userPassword.value,
                onValueChange = { userPassword.value = it },
                label = { Text("Password") },
                placeholder = { Text("Enter your password") }
            )
            Spacer(modifier = Modifier.size(20.dp))

            when {
                state.value.isLoading -> {

                    GrowingDotsProgressBar(
                        modifier = Modifier.size(80.dp),
                        size = 80.dp,
                        color = Color.Magenta
                    )
                }

                state.value.error != null -> {
                    Text(
                        text = state.value.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                state.value.data != null -> {
                    LaunchedEffect(state.value.data) {
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.HomeScreen) {
                            popUpTo(Routes.LogInScreen) { inclusive = true }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (userEmail.value.isNotEmpty() && userPassword.value.isNotEmpty()) {
                        viewModel.logInView(userEmail.value, userPassword.value)
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.size(20.dp))

            MultiColorText(
                firstText = "Don't have an account? ",
                secondText = "Sign Up",
                modifier = Modifier.clickable {
                    navController.navigate(Routes.SignUpScreen) {
                        popUpTo(Routes.LogInScreen) { inclusive = true }
                    }
                }
            )
        }
    }
}