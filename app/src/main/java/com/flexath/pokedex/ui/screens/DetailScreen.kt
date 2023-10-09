package com.flexath.pokedex.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.flexath.pokedex.R
import com.flexath.pokedex.data.vos.detail.Type
import com.flexath.pokedex.network.responses.PokedexResponse
import com.flexath.pokedex.ui.view_models.PokedexDetailViewModel
import com.flexath.pokedex.utils.Resource
import com.flexath.pokedex.utils.parseStatToAbbr
import com.flexath.pokedex.utils.parseStatToColor
import com.flexath.pokedex.utils.parseTypeToColor
import java.util.Locale
import kotlin.math.roundToInt

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
                    bottom = 16.dp
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
            pokedexInfo.data?.let {
                PokedexDetailSection(
                    pokedexInfo = it,
                    modifier = modifier.offset(y = (-20).dp)
                )
            }
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

@Composable
fun PokedexDetailSection(
    pokedexInfo: PokedexResponse,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${pokedexInfo.id} ${
                pokedexInfo.name.replaceFirstChar {
                    if (it.isLowerCase()) {
                        it.titlecase(
                            Locale.ROOT
                        )
                    } else {
                        it.toString()
                    }
                }
            }",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        PokedexTypeSection(pokedexInfo.types)

        PokedexDetailDataItemSection(
            pokedexWeight = pokedexInfo.weight,
            pokedexHeight = pokedexInfo.height
        )

        PokedexDetailBaseStats(pokedexInfo)
    }
}

@Composable
fun PokedexTypeSection(
    types: List<Type>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        for (type in types) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .background(color = parseTypeToColor(type), shape = CircleShape)
                    .clip(CircleShape)
                    .wrapContentHeight()
            ) {
                Text(
                    text = type.type.name.replaceFirstChar {
                        if (it.isLowerCase()) {
                            it.titlecase(
                                Locale.ROOT
                            )
                        } else {
                            it.toString()
                        }
                    },
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )


            }
        }
    }
}

@Composable
fun PokedexDetailDataItemSection(
    pokedexWeight: Int,
    pokedexHeight: Int,
    sectionHeight: Dp = 80.dp
) {
    val pokedexWeightInKg = remember {
        (pokedexWeight * 100f).roundToInt() / 1000f
    }

    val pokedexHeightInMeter = remember {
        (pokedexHeight * 100f).roundToInt() / 1000f
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        PokedexDetailDataItem(
            dataValue = pokedexWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )

        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.DarkGray)
        )

        PokedexDetailDataItem(
            dataValue = pokedexHeightInMeter,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PokedexDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            painter = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PokedexDetailBaseStats(
    pokedexInfo: PokedexResponse,
    animDelayPerItem: Int = 100
) {
    val maxBaseStat = remember {
        pokedexInfo.stats.maxOf {
            it.base_stat
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Base Stats :",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        for (i in pokedexInfo.stats.indices) {
            val stat = pokedexInfo.stats[i]

            PokedexStat(
                statName = parseStatToAbbr(stat = stat),
                statValue = stat.base_stat,
                statMaxValue = maxBaseStat,
                statColor = parseStatToColor(stat),
                animDuration = i * animDelayPerItem,
                animDelay = animDelayPerItem
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun PokedexStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 40.dp,
    animDuration: Int = 100,
    animDelay: Int = 0
) {
    var animPlayed by remember {
        mutableStateOf(false)
    }

    val currentPercent = animateFloatAsState(
        targetValue = if (animPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        label = "",
        animationSpec = tween(animDuration, animDelay)
    )

    LaunchedEffect(key1 = true) {
        animPlayed = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = height)
            .clip(CircleShape)
            .background(
                color = if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(currentPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = (currentPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetail() {
    DetailScreen(rememberNavController(), Color(1), "pokedexName")
}