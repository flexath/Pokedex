package com.flexath.pokedex.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun DetailScreen(navController: NavHostController, dominantColor: Int?, pokedexName: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            TextButton(
                onClick = {
                    navController.popBackStack()
                }, border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Back to Home")
            }

            Text(
                dominantColor.toString(),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start),
                fontSize = 20.sp
            )

            Text(
                pokedexName.toString(), modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Start),
                fontSize = 20.sp
            )
        }

    }
}

@Preview(showBackground = true)
@Composable()
fun PreviewDetail() {
    DetailScreen(rememberNavController(), 0, "pokedexName")
}