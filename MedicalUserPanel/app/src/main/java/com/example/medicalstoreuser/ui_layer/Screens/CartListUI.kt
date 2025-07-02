package com.example.medicalstoreuser.ui_layer.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medicalstoreuser.ui_layer.AppViewModel

@Composable
fun CartListUI(navController: NavController ) {

        val viewModel: AppViewModel = hiltViewModel()
        val cartState = viewModel.cartAddedResponse.collectAsState() // Cart product IDs
        val productsState = viewModel.getAllProductsResponse.collectAsState() // All products

        when {
            productsState.value.isLoading -> {
                Text(text = "Loading...")
            }
            productsState.value.error != null -> {
                Text(text = "Error: ${productsState.value.error}")
            }
            productsState.value.data != null -> {
                // Once data is ready, render the cart items
                val cartProductIds = cartState.value
                val allProducts = productsState.value.data?.body()

                if (cartProductIds.isEmpty()) {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {

                        Text(text = "Your cart is empty.", fontSize = 24.sp)
                    }
                } else {
                    LazyColumn {
                        items(cartProductIds) { productId ->

                            val product = allProducts?.find { it.product_id == productId }

                            if (product != null) {
                                Card(
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .wrapContentSize(),
                                    elevation = CardDefaults.cardElevation(8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(text = "Product Name: ${product.product_name}")
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "Price: ${product.price}")
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "Expiry Date: ${product.expiry_date}")
                                    }
                                }
                            } else {
                                Text(text = "Product not found")
                            }
                        }
                    }
                }
            }
        }
    }

