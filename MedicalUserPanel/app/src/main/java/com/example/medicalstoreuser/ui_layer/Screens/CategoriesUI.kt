package com.example.medicalstoreuser.ui_layer.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medicalstoreuser.ui_layer.AppViewModel

@Composable
fun CategoriesUI(navController: NavController) {

    val viewModel: AppViewModel = hiltViewModel()
    val state = viewModel.getAllProductsResponse.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 10.dp,
                    vertical = 5.dp
                )
        ) {
            Text(
                text = "Categories",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            LazyColumn {
                items(state.value.data?.body() ?: emptyList()) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    ) {
                        Text(
                            text = " ->  ${it.category}",
                            modifier = Modifier.padding(10.dp),
                            style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                        )
                    }

                }
            }
        }
    }
}