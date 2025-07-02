package com.example.medicalstoreadmin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medicalstoreadmin.ui_layer.navigations.Routes


@Composable
fun MyApp(viewModel: AppViewModel = hiltViewModel() , userID : String? = "" , navController: NavController , value : Int? = 0) {

    var showRecheckCard = remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        if (showRecheckCard.value) {
            // Apply blur effect to the background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .blur(16.dp)
            )

            // Display the Recheck Card
            Dialog(
                onDismissRequest = { showRecheckCard.value = false },
                properties = DialogProperties(dismissOnClickOutside = false)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(16.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Recheck Information")
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Are you sure you want to proceed?")
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = { showRecheckCard.value = false
                                navController.navigate(Routes.HomeScreen)
                            }) {
                                Text("Cancel")
                            }

                            Button(onClick = {
                                // Action for proceed
                                showRecheckCard.value = false

                                viewModel.approveUser(
                                    user_id = userID.toString(),
                                    isApproved = value?.toInt() ?: 0
                                )

                                navController.navigate(Routes.HomeScreen)
                            }) {
                                Text("Proceed")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun test(modifier: Modifier = Modifier) {
    MyApp(navController = NavController(context = androidx.compose.ui.platform.LocalContext.current), userID = "12345", value = 1)
}