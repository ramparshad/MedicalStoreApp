package com.example.medicalstoreuser.ui_layer.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medicalstoreuser.R
import com.example.medicalstoreuser.ui_layer.AppViewModel
import com.example.medicalstoreuser.ui_layer.common.MultiColorText
import com.example.medicalstoreuser.ui_layer.navigation.Routes

@Composable
fun SignUpScreenUI(
    navController: NavController,
    viewModel: AppViewModel = hiltViewModel()
) {
    val signUpState by viewModel.signUpUserState.collectAsState()
    val context = LocalContext.current

    val userName = remember { mutableStateOf("") }
    val userEmail = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }
    val userPhoneNumber = remember { mutableStateOf("") }
    val userAddress = remember { mutableStateOf("") }
    val userPinCode = remember { mutableStateOf("") }

    // Show Toast for errors or success
    LaunchedEffect(signUpState.error) {
        if (signUpState.error != null) {
            Toast.makeText(context, signUpState.error, Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(signUpState.data) {
        if (signUpState.data != null) {
            Toast.makeText(context, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.HomeScreen) {
                popUpTo(Routes.SignUpScreen) { inclusive = true }
            }
        }
    }

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
                value = userName.value,
                onValueChange = { userName.value = it },
                label = { Text("Name") },
                placeholder = { Text("Enter your name") }
            )
            Spacer(modifier = Modifier.size(10.dp))

            OutlinedTextField(
                value = userEmail.value,
                onValueChange = { userEmail.value = it },
                label = { Text("Email") },
                placeholder = { Text("Enter your email") }
            )
            Spacer(modifier = Modifier.size(10.dp))

            OutlinedTextField(
                value = userPhoneNumber.value,
                onValueChange = { userPhoneNumber.value = it },
                label = { Text("Phone Number") },
                placeholder = { Text("Enter your phone number") }
            )
            Spacer(modifier = Modifier.size(10.dp))

            OutlinedTextField(
                value = userPassword.value,
                onValueChange = { userPassword.value = it },
                label = { Text("Password") },
                placeholder = { Text("Enter your password") }
            )
            Spacer(modifier = Modifier.size(10.dp))

            OutlinedTextField(
                value = userAddress.value,
                onValueChange = { userAddress.value = it },
                label = { Text("Address") },
                placeholder = { Text("Enter your address") }
            )
            Spacer(modifier = Modifier.size(10.dp))

            OutlinedTextField(
                value = userPinCode.value,
                onValueChange = { userPinCode.value = it },
                label = { Text("Pin Code") },
                placeholder = { Text("Enter your pin code") }
            )
            Spacer(modifier = Modifier.size(20.dp))

            Button(
                onClick = {
                    viewModel.signUpView(
                        name = userName.value,
                        email = userEmail.value,
                        phoneNumber = userPhoneNumber.value,
                        address = userAddress.value,
                        password = userPassword.value,
                        pinCode = userPinCode.value
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }

            when {
                signUpState.isLoading -> {
                    Spacer(modifier = Modifier.size(20.dp))
                    CircularProgressIndicator()
                }
                signUpState.error != null -> {
                    Spacer(modifier = Modifier.size(20.dp))
                    Text("Error: ${signUpState.error}", color = MaterialTheme.colorScheme.error)
                }
                else -> {}
            }

            Spacer(modifier = Modifier.size(40.dp))

            MultiColorText(
                firstText = "Already have an account? ",
                secondText = "Login",
                modifier = Modifier.clickable {
                    navController.navigate(Routes.LogInScreen) {
                        popUpTo(Routes.SignUpScreen) { inclusive = true }
                    }
                }
            )
        }
    }
}


