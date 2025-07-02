package com.example.medicalstoreadmin.ui_layer.screens

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.example.medicalstoreadmin.ui_layer.navigations.Routes.UserDetailScreen

@Composable
fun HomeScreenUI (navController: NavController, viewModel : AppViewModel = hiltViewModel()){

    val state = viewModel.getAllUsersResponse.collectAsState()
    val state1 = viewModel.updateUserDetailsResponse.collectAsState()
    val state2 = viewModel.deleteSpecificUserResponse.collectAsState()

    val context = LocalContext.current

    when{
        state1.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }
        state1.value.error != null -> {
            Log.e("UI_ERROR", state1.value.error.toString())
            Box(modifier = Modifier.fillMaxSize()){
                Text(text = state1.value.error.toString())
            }
        }
        state1.value.data != null -> {
            Toast.makeText(context, "User Details Updated", Toast.LENGTH_SHORT).show()

            LaunchedEffect(Unit) {
                viewModel.getAllUsers()
            }
        }
    }

    when{
        state2.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator()
            }
        }
        state2.value.error != null -> {
            Log.e("UI_ERROR", state2.value.error.toString())
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = state2.value.error.toString())
            }
        }
        state2.value.data != null -> {
            Toast.makeText(context, "User Deleted", Toast.LENGTH_SHORT).show()

            LaunchedEffect(state2.value.data) {
                viewModel.getAllUsers()
            }
        }
    }

    when{
        state.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator()
            }
        }
        state.value.error != null -> {
            Box(modifier = Modifier.fillMaxSize()){
                Text(text = state.value.error.toString())
            }
        }
        state.value.data != null -> {

            if (state.value.data?.body() != null) {

                LazyColumn {

                    items(state.value.data?.body() ?: emptyList()) {

                        if (it.isApproved == 0) {
                            Card(modifier = Modifier.scale(0.95f).clickable {
                                navController.navigate(UserDetailScreen)
                            }) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Column(
                                        modifier = Modifier.wrapContentWidth().width(200.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = it.name ?: "No Name",
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = it.email ?: "No Email")
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = it.phone_number ?: "No Phone Number")
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Password : ${it.password}" ?: "No Password"
                                        )

                                        if (it.isApproved == 0) {
                                            Button(
                                                onClick = {
                                                    navController.navigate(
                                                        Routes.CheckOutScreen(
                                                            userID = it.user_id,
                                                            value = 1
                                                        )
                                                    )
                                                }
                                            ) {
                                                Text(text = "Approve")
                                            }
                                        } else {
                                            Button(
                                                onClick = {
                                                    navController.navigate(
                                                        Routes.CheckOutScreen(
                                                            userID = it.user_id,
                                                            value = 0
                                                        )
                                                    )
                                                },
                                            ) {
                                                Text(text = "Block")
                                            }
                                        }
                                    }
                                    Column(
                                        modifier = Modifier.wrapContentWidth().width(200.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        Text(text = it.pinCode ?: "No Pin Code")
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = it.id.toString() ?: "No ID")
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = it.isApproved.toString() ?: "No Level")

                                        Button(
                                            onClick = {
                                                viewModel.deleteSpecificUser(it.user_id)
                                                Log.d(
                                                    "TAG",
                                                    "Deleting user with ID: ${it.user_id}"
                                                )
                                            },
                                            modifier = Modifier.padding(
                                                start = 4.dp,
                                                end = 4.dp
                                            )
                                                .align(Alignment.CenterHorizontally)
                                        ) {
                                            Text(text = "Delete")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else{
                Box(modifier = Modifier.fillMaxSize()){
                    Text(text = "Data is Coming")
                }
            }
        }
    }
}