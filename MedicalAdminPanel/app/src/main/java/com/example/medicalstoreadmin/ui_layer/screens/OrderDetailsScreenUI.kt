package com.example.medicalstoreadmin.ui_layer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medicalstoreadmin.AppViewModel

@Composable
fun OrderDetailsScreenUI(
    orderID: String,
    viewModel: AppViewModel = hiltViewModel(),
    userID: String? = "",
    category: String,
    orderDate: String,
    productID: String,
    isApproved: String,
    productExpiryDate: String? = "",
    productQuantity: String? = "",
    totalPrice: String? = "",
) {

    Card(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(text = orderID.toString())
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = userID.toString() ?: "No UserID")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = category.toString())
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = orderDate.toString())
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = productID.toString())
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = isApproved.toString())
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = productExpiryDate.toString() ?: "No Expiry Date")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = productQuantity.toString() ?: "No Quantity")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = totalPrice.toString() ?: "No Total Price")
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}