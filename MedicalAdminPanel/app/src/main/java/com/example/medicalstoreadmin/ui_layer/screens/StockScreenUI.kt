package com.example.medicalstoreadmin.ui_layer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medicalstoreadmin.AppViewModel

@Composable
fun StockScreenUI(navController: NavController, viewModel: AppViewModel = hiltViewModel()) {
    val state = viewModel.getAllProductsResponse.collectAsState()
    when {
        state.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
        state.value.error != null -> {
            Box(modifier = Modifier.fillMaxSize()) {
                state.value.error?.let {
                    Text(text = it)
                }
            }
        }
        state.value.data != null -> {
            if (state.value.data?.body() != null) {
                LazyColumn {
                    items(state.value.data?.body() ?: emptyList()) {
                        Card(modifier = Modifier.scale(0.95f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .width(200.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = it.id.toString() ?: "No Id",
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = it.category ?: "No Category")
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = it.expiry_date ?: "No Expiry Date")
                                    Spacer(modifier = Modifier.height(6.dp))

                                }
                                Column(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .width(200.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = it.price.toString() ?: "No Price")
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = it.product_name ?: "No Product Name")
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = it.stock.toString() ?: "No Stock")
                                    Button(
                                        onClick = { },
                                        modifier = Modifier
                                            .padding(start = 4.dp, end = 4.dp)
                                            .align(Alignment.CenterHorizontally)
                                    ) {
                                        Text(text = "Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "No Data Found")
                }
            }
        }
    }
}