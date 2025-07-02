package com.example.medicalstoreadmin.ui_layer.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medicalstoreadmin.AppViewModel
import com.example.medicalstoreadmin.ui_layer.navigations.Routes

@Composable
fun OrderScreenUI(navController: NavController, viewModel: AppViewModel = hiltViewModel()) {

    val state = viewModel.getAllOrdersResponse.collectAsState()
    val productState = viewModel.getSpecificProductResponse.collectAsState()
    val updateState = viewModel.updateOrderDetailsResponse.collectAsState()
    val deleteOrderState = viewModel.deleteSpecificOrderResponse.collectAsState()

    val context = LocalContext.current
    val data = state.value.data?.body()

    when {
        updateState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
        updateState.value.error != null -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = updateState.value.error.toString())

            }
        }
        updateState.value.data != null -> {
            Toast.makeText(context, "Order Details Updated", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle delete responses
    LaunchedEffect(deleteOrderState.value) {
        when {
            deleteOrderState.value.isLoading -> {
                Toast.makeText(context, "Deleting order...", Toast.LENGTH_SHORT).show()
            }
            deleteOrderState.value.error != null -> {
                Toast.makeText(context, "Deletion failed: ${deleteOrderState.value.error}", Toast.LENGTH_SHORT).show()
            }
            deleteOrderState.value.data?.isSuccessful == true -> {
                Toast.makeText(context, "Order deleted successfully", Toast.LENGTH_SHORT).show()
                viewModel.getAllOrders() // Refresh the list
            }
            deleteOrderState.value.data?.isSuccessful == false -> {
                Toast.makeText(context, "Deletion failed: ${deleteOrderState.value.data?.message()}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    when {
        state.value.isLoading -> {
            CircularProgressIndicator()
        }
        state.value.error != null -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = state.value.error.toString())
            }
        }
        state.value.data != null -> {
            if (data != null) {
                LazyColumn {
                    items(data) {

                        Card(modifier = Modifier
                            .scale(0.95f)
                            .clickable {
                                navController.navigate(
                                    Routes.OrderDetailScreen(
                                        orderID = it.order_id,
                                        userID = it.user_id,
                                        category = it.category,
                                        orderDate = it.order_date,
                                        productID = it.product_id,
                                        isApproved = it.isApproved.toString(),
                                        productExpiryDate = it.product_expiry_date,
                                        productQuantity = it.quantity.toString(),
                                        totalPrice = it.total_price.toString()
                                    )
                                )
                            }) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = it.id.toString() ?: "No ID",
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = ("Category : " + it.category) ?: "No Category")
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = ("Order Date : " + it.order_date) ?: "No Order Date")
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = ("Approved : " + it.isApproved.toString()) ?: "No Level"
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = ("Quantity : " + it.quantity.toString()) ?: "No Quantity"
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = ("Total Price : " + it.total_price.toString())
                                        ?: "No Price"
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = ("Product Expiry Date : " + it.product_expiry_date)
                                        ?: "No Expiry Date"
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    if (it.isApproved == 0) {
                                        Button(
                                            onClick = {
                                                viewModel.updateOrderDetails(
                                                    order_id = it.order_id,
                                                    isApproved = 1
                                                )
                                                viewModel.getAllOrders()
                                                viewModel.getSpecificProduct(product_id = it.product_id)
                                                when {
                                                    productState.value.error != null -> {
                                                        Toast.makeText(
                                                            context,
                                                            productState.value.error,
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                    productState.value.data != null -> {
                                                        viewModel.updateProductDetails(
                                                            it.product_id,
                                                            stock = (productState.value.data!!.body()?.stock?.minus(
                                                                it.quantity
                                                            )).toString()
                                                        ) } } },
                                        ) {
                                            Text(text = "Approve")
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                viewModel.updateOrderDetails(
                                                    order_id = it.order_id,
                                                    isApproved = 0
                                                )
                                                viewModel.getAllOrders()
                                            },
                                        ) {
                                            Text(text = "Reject")
                                        }
                                    }
                                    Button(
                                        onClick = {
                                            viewModel.deleteSpecificOrder(it.order_id)
                                        },
                                        modifier = Modifier.padding(
                                            start = 4.dp,
                                            end = 4.dp
                                        )
                                    ) {
                                        Text(text = "Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Text(text = "Data is Coming")
            }
        }
    }
}



