package com.flexath.pokedex.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.flexath.pokedex.R
import com.flexath.pokedex.ui.models.PokedexEntryItem
import com.flexath.pokedex.ui.nav_graph.Screen
import com.flexath.pokedex.ui.theme.RobotoCondensed
import com.flexath.pokedex.ui.view_models.PokedexListViewModel
import timber.log.Timber

@Composable
fun HomeScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )

            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 32.dp
                    ),
                hint = "Search Pokemon...",
                onSearchText = {
                    Timber.tag("SearchText : ").i(it)
                })

            PokedexEntryList(entryList = pokeList, navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String,
    onSearchText: (String) -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearchText(text)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(elevation = 5.dp, shape = CircleShape)
                .background(color = Color.White, shape = CircleShape)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .onFocusChanged {
                    isHintDisplayed != it.isFocused
                }
        )

        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                fontSize = 16.sp
            )
        }

        isHintDisplayed = text.isEmpty()
    }
}

@Composable
fun PokedexEntryList(
    entryList : List<PokedexEntryItem>,
    navController: NavHostController
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 140.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(entryList) {
            PokedexEntry(
                entry = it,
                navController = navController
            )
        }
    }
}

val pokeList = listOf(
    PokedexEntryItem("Bill 1","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",1),
    PokedexEntryItem("Bill 2","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png",2),
    PokedexEntryItem("Bill 3","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/3.png",3),
    PokedexEntryItem("Bill 4","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png",4),
    PokedexEntryItem("Bill 5","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/5.png",5),
    PokedexEntryItem("Bill 6","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png",6),
    PokedexEntryItem("Bill 7","https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png",7),
)

@Composable
fun PokedexEntry(
    entry: PokedexEntryItem,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: PokedexListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colorScheme.surface

    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .shadow(elevation = 5.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .aspectRatio(1f)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            dominantColor,
                            defaultDominantColor
                        )
                    )
                )
                .clickable {
                    navController.navigate(
                        Screen.Detail.passByColorAndName(dominantColor.toArgb(), entry.pokedexName)
                    )
                }
        ) {
            Column {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(entry.imageUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.pokeball)
                        .error(R.drawable.pokeball_colorless)
                        .build(),
                    contentDescription = "Pokemon",
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.Medium,
                    loading = {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.scale(0.5f)
                        )
                    },
                    onSuccess = {
                        viewModel.calculateDominantColor(it.result.drawable, onFinish = { color ->
                            dominantColor = color
                        })
                    },
                    modifier = Modifier
                        .size(120.dp)
                        .align(CenterHorizontally)
                )

                Text(
                    text = entry.pokedexName,
                    fontFamily = RobotoCondensed,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize().padding(bottom = 20.dp)
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable()
fun PreviewHome() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        contentColor = MaterialTheme.colorScheme.background
    ) {
        HomeScreen(rememberNavController())
    }
}