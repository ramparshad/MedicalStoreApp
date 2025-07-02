package com.example.medicalstoreuser.ui_layer.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.medicalstoreuser.ui_layer.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun StartScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Column(
            modifier=Modifier.fillMaxSize().padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                model = "https://image.freepik.com/free-vector/medical-store-logo-template-design_316488-1327.jpg",
                contentDescription = "Splash Image",
                modifier = Modifier
                    .height(300.dp)
                    .width(300.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Text(text = "Welcome to Medical Store User Panel",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LaunchedEffect(Unit) {
            delay(1500) // Brief splash screen for 0.5 seconds
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                navController.navigate(Routes.HomeScreen) {
                    popUpTo(Routes.StartScreen) { inclusive = true }
                }
            } else {
                navController.navigate(Routes.LogInScreen) {
                    popUpTo(Routes.StartScreen) { inclusive = true }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenUIPreview() {
    StartScreen(navController = NavHostController(LocalContext.current))
}

