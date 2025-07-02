package com.example.medicalstoreuser.ui_layer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.medicalstoreuser.R
import com.example.medicalstoreuser.ui_layer.common.MultiColorText

@Preview(showBackground = true , showSystemUi = true)
@Composable
fun TestCompose(modifier: Modifier = Modifier){

    Row {
        SubcomposeAsyncImage(
            model = "https://www.northjersey.com/gcdn/authoring/authoring-images/2024/10/19/PNJM/75756774007-powerball-1019.jpg?crop=1440,810,x124,y219&width=1440&height=810&format=pjpg&auto=webp",
            contentDescription = null,
            loading = {
                CircularProgressIndicator(modifier = modifier.size(50.dp))
            },
            modifier = Modifier.size(150.dp).padding(end = 8.dp),
            error = {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "Placeholder Image",
                    modifier = Modifier.size(150.dp)
                )
            }
        )
    }
}

