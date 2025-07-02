package com.example.medicalstoreadmin.ui_layer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
fun UserDetailsScreenUI(navController: NavController , viewModel : AppViewModel = hiltViewModel() ){

    val state = viewModel.getAllUsersResponse.collectAsState()

    Card(modifier = Modifier.scale(0.95f)) {

            Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround){

            Column (
                modifier = Modifier.wrapContentWidth().width(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = state.value.data?.body()?.get(0)?.name.toString(), modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = state.value.data?.body()?.get(0)?.email.toString(),)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = state.value.data?.body()?.get(0)?.phone_number.toString())
                Button(onClick = {

                },
                ) {
                    Text(text = "Approve")
                }
            }
            Column (modifier = Modifier.wrapContentWidth().width(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = state.value.data?.body()?.get(0)?.pinCode.toString())
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = state.value.data?.body()?.get(0)?.id.toString())
                Button(
                    onClick = {

                    },
                    modifier = Modifier.padding(start = 4.dp , end = 4.dp).align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}