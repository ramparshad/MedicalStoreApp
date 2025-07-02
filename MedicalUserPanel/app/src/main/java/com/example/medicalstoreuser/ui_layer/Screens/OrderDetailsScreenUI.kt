package com.example.medicalstoreuser.ui_layer.Screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medicalstoreuser.ui_layer.AppViewModel
import com.example.medicalstoreuser.ui_layer.navigation.Routes
import com.example.medicalstoreuser.user_pref.UserPreferenceManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderDetailsScreenUI(product_id: String, navController: NavController, userPreferenceManager: UserPreferenceManager) {
    val viewModel = hiltViewModel<AppViewModel>()
    val context = LocalContext.current
    val state = viewModel.createOrderResponse.collectAsState()
    val state2 = viewModel.getSpecificProductResponse.collectAsState()
    val user = userPreferenceManager.userID.collectAsState(initial = "")
    var quantity by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf(LocalDate.now().year.toString()) }
    var dateError by remember { mutableStateOf<String?>(null) }

    // Trigger product fetch if needed
    LaunchedEffect(Unit) {
        viewModel.getSpecificProduct(product_id)
    }

    // Define custom colors for consistent theming
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val surfaceColor = MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        when {
            state.value.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = primaryColor
                )
            }
            state.value.error != null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = errorColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Error: ${state.value.error}",
                        color = errorColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.getSpecificProduct(product_id) },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Retry")
                    }
                }
            }
            state.value.data != null -> {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, "Order Placed Successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.OrderHistory) {
                        popUpTo(Routes.OrderDetailsScreen(product_id)) { inclusive = true }
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Text(
                        text = "Place Your Order",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    )

                    // Product Info (if available)
                    state2.value.data?.body()?.let { product ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = surfaceColor)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = product.product_name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Available Stock: ${product.stock}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Quantity Input
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                                quantity = newValue
                            }
                        },
                        label = { Text("Quantity") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = quantity.isNotEmpty() && (quantity.toIntOrNull() ?: 0) <= 0,
                        supportingText = {
                            if (quantity.isNotEmpty() && (quantity.toIntOrNull() ?: 0) <= 0) {
                                Text("Quantity must be greater than 0", color = errorColor)
                            }
                        }
                    )

                    Text(
                        text = "Select Order Date",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.align(Alignment.Start)
                    )

                    // Date Input (Day, Month, Year)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = day,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                                    day = newValue
                                }
                            },
                            label = { Text("Day") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = day.isNotEmpty() && (day.toIntOrNull() ?: 0) !in 1..31,
                            supportingText = {
                                if (day.isNotEmpty() && (day.toIntOrNull() ?: 0) !in 1..31) {
                                    Text("Day must be 1-31", color = errorColor)
                                }
                            }
                        )
                        OutlinedTextField(
                            value = month,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                                    month = newValue
                                }
                            },
                            label = { Text("Month") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = month.isNotEmpty() && (month.toIntOrNull() ?: 0) !in 1..12,
                            supportingText = {
                                if (month.isNotEmpty() && (month.toIntOrNull() ?: 0) !in 1..12) {
                                    Text("Month must be 1-12", color = errorColor)
                                }
                            }
                        )
                        OutlinedTextField(
                            value = year,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                                    year = newValue
                                }
                            },
                            label = { Text("Year") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = year.isNotEmpty() && (year.toIntOrNull() ?: 0) !in 2023..2100,
                            supportingText = {
                                if (year.isNotEmpty() && (year.toIntOrNull() ?: 0) !in 2023..2100) {
                                    Text("Year must be 2023-2100", color = errorColor)
                                }
                            }
                        )
                    }

                    // Date Error Message
                    dateError?.let {
                        Text(
                            text = it,
                            color = errorColor,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    // Place Order Button
                    Button(
                        onClick = {
                            val qty = quantity.toIntOrNull() ?: 0
                            val stock = state2.value.data?.body()?.stock ?: 0
                            when {
                                quantity.isEmpty() -> {
                                    Toast.makeText(context, "Please enter quantity", Toast.LENGTH_SHORT).show()
                                }
                                qty <= 0 -> {
                                    Toast.makeText(context, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show()
                                }
                                qty > stock -> {
                                    Toast.makeText(context, "Out of Stock: Only $stock available", Toast.LENGTH_SHORT).show()
                                }
                                day.isEmpty() || month.isEmpty() || year.isEmpty() -> {
                                    Toast.makeText(context, "Please enter a complete date", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    val dateString = "$year-${month.padStart(2, '0')}-${day.padStart(2, '0')}"
                                    try {
                                        val parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
                                        dateError = null
                                        viewModel.createOrder(
                                            userId = user.value.toString(),
                                            productId = product_id,
                                            quantity = quantity,
                                            orderDate = dateString
                                        )
                                    } catch (e: DateTimeParseException) {
                                        dateError = "Invalid date. Please check day, month, and year."
                                        Toast.makeText(context, "Invalid date", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        enabled = quantity.isNotEmpty() && day.isNotEmpty() && month.isNotEmpty() && year.isNotEmpty()
                    ) {
                        Text(
                            text = "Place Order",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}