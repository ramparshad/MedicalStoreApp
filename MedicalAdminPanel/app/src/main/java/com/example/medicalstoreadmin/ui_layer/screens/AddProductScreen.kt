package com.example.medicalstoreadmin.ui_layer.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medicalstoreadmin.AppViewModel

@
fun AddProductScreenUI(navController: NavController ,viewModel: AppViewModel = hiltViewModel()) {

    val state = viewModel.addProductResponse.collectAsState()

    val context = LocalContext.current

    var ProductName = remember { mutableStateOf("") }
    var ProductExpiryDate = remember { mutableStateOf("") }
    var ProductPrice = remember { mutableStateOf("") }
    var ProductStock = remember { mutableStateOf("") }
    var ProductCategory = remember { mutableStateOf("") }

    var response = remember { mutableStateOf<Int?>(null) }

    when {
        state.value.isLoading -> {
            Box {
                CircularProgressIndicator()
            }
        }
        state.value.error != null -> {
            Log.e("UI_ERROR", state.value.error.toString())
            Box(modifier = Modifier.fillMaxSize()){
                Text(text = state.value.error.toString())
            }
        }
        state.value.data != null -> {
            Toast.makeText(context, "${state.value.data?.body()?.message}" , Toast.LENGTH_SHORT).show()
        LaunchedEffect(Unit){
                        ProductName.value = ""
                        ProductExpiryDate.value = ""
                        ProductPrice.value = ""
                        ProductStock.value = ""
                        ProductCategory.value = ""
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        CustomTextField(
            value = ProductName.value,
            onValueChange = { ProductName.value = it },
            label = "Product Name",
            leadingIcon = Icons.Default.Create,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        CustomTextField(
            value = ProductPrice.value,
            onValueChange = { ProductPrice.value = it },
            label = "Product Price",
            leadingIcon = Icons.Default.Create,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        CustomTextField(
            value = ProductCategory.value,
            onValueChange = { ProductCategory.value = it },
            label = "Product Usage",
            leadingIcon = Icons.Default.Create,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        CustomTextField(
            value = ProductStock.value,
            onValueChange = { ProductStock.value = it },
            label = "Product Stock",
            leadingIcon = Icons.Default.Create,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(4.dp))

        CustomTextField(
            value = ProductExpiryDate.value,
            onValueChange = { ProductExpiryDate.value = it },
            label = "Product Expiry Date",
            leadingIcon = Icons.Default.Create,
            modifier = Modifier.fillMaxWidth()
        )

        LaunchedEffect(response) {
            if(state.value.data != null) {
                response.let {
                    val message = if (it.value == 200) {
                        "Product added successfully"
                    } else {
                        "Failed to add product"
                    }
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(context, "Please fill all the data", Toast.LENGTH_SHORT).show()
            }
        }

        Button(
            onClick = {
                if (ProductName.value.isNotEmpty() && ProductExpiryDate.value.isNotEmpty() && ProductPrice.value.isNotEmpty() && ProductStock.value.isNotEmpty() && ProductCategory.value.isNotEmpty()) {
                    viewModel.addProduct(
                        product_name = ProductName.value,
                        product_expiry_date = ProductExpiryDate.value,
                        product_price = ProductPrice.value,
                        stock = ProductStock.value,
                        product_category = ProductCategory.value
                    )
                    response.value = 200
                }
                    else{
                      Toast.makeText(context, "Please fill all the data", Toast.LENGTH_SHORT).show()
                    }
                }
        ){
            Text(text = "Add Product")
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier
) {
    TextField(value = value, onValueChange = onValueChange , label = { Text(text = label) } , leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },modifier = modifier)
}
