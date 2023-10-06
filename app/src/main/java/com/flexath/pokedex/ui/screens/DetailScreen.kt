package com.flexath.pokedex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.flexath.pokedex.R
import com.flexath.pokedex.network.responses.PokedexResponse
import com.flexath.pokedex.ui.view_models.PokedexDetailViewModel
import com.flexath.pokedex.utils.Resource

@Composable
fun DetailScreen(
    navController: NavHostController,
    dominantColor: Color,
    pokedexName: String,
    topPadding: Dp = 20.dp,
    pokedexImageSize: Dp = 200.dp,
    viewModel: PokedexDetailViewModel = hiltViewModel()
) {
    val pokedexInfo = produceState<Resource<PokedexResponse>>(
        initialValue = Resource.Loading()
    ) {
        value = viewModel.getPokedexInfo(pokedexName)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {

        PokedexTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )

        PokedexDetailStateWrapper(
            pokedexInfo = pokedexInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    top = topPadding + pokedexImageSize / 2f,
                    end = 20.dp,
                    bottom = 20.dp,
                )
                .shadow(elevation = 3.dp, shape = RoundedCornerShape(10.dp))
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    start = 16.dp,
                    top = topPadding + pokedexImageSize / 2f,
                    end = 16.dp,
                    bottom = 16.dp,
                )
        )

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            if (pokedexInfo is Resource.Success) {
                pokedexInfo.data?.sprites?.let {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.front_default)
                            .crossfade(true)
                            .placeholder(R.drawable.pokeball)
                            .error(R.drawable.pokeball_colorless)
                            .build(),
                        contentDescription = pokedexName,
                        contentScale = ContentScale.Crop,
                        filterQuality = FilterQuality.Medium,
                        loading = {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.scale(0.3f)
                            )
                        },
                        modifier = Modifier
                            .size(pokedexImageSize)
                            .offset(y = topPadding)
                    )
                }


            }
        }
    }
}

@Composable
fun PokedexTopSection(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
            .padding(top = 30.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back Button",
            tint = Color.White,
            modifier = Modifier
                .size(30.dp)
                .offset(x = 20.dp, y = 20.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun PokedexDetailStateWrapper(
    pokedexInfo: Resource<PokedexResponse>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when (pokedexInfo) {
        is Resource.Success -> {

        }

        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = loadingModifier
            )
        }

        is Resource.Error -> {
            Text(
                text = pokedexInfo.message ?: "",
                color = Color.Red,
                modifier = modifier
            )

        }
    }
}

@Preview(showBackground = true)
@Composable()
fun PreviewDetail() {
    DetailScreen(rememberNavController(), Color(1), "pokedexName")
}