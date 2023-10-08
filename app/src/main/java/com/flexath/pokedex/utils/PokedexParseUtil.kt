package com.flexath.pokedex.utils

import androidx.compose.ui.graphics.Color
import com.flexath.pokedex.data.vos.detail.Stat
import com.flexath.pokedex.data.vos.detail.Type
import com.flexath.pokedex.ui.theme.AtkColor
import com.flexath.pokedex.ui.theme.DefColor
import com.flexath.pokedex.ui.theme.HPColor
import com.flexath.pokedex.ui.theme.SpAtkColor
import com.flexath.pokedex.ui.theme.SpDefColor
import com.flexath.pokedex.ui.theme.SpdColor
import com.flexath.pokedex.ui.theme.TypeBug
import com.flexath.pokedex.ui.theme.TypeDark
import com.flexath.pokedex.ui.theme.TypeDragon
import com.flexath.pokedex.ui.theme.TypeElectric
import com.flexath.pokedex.ui.theme.TypeFairy
import com.flexath.pokedex.ui.theme.TypeFighting
import com.flexath.pokedex.ui.theme.TypeFire
import com.flexath.pokedex.ui.theme.TypeFlying
import com.flexath.pokedex.ui.theme.TypeGhost
import com.flexath.pokedex.ui.theme.TypeGrass
import com.flexath.pokedex.ui.theme.TypeGround
import com.flexath.pokedex.ui.theme.TypeIce
import com.flexath.pokedex.ui.theme.TypeNormal
import com.flexath.pokedex.ui.theme.TypePoison
import com.flexath.pokedex.ui.theme.TypePsychic
import com.flexath.pokedex.ui.theme.TypeRock
import com.flexath.pokedex.ui.theme.TypeSteel
import com.flexath.pokedex.ui.theme.TypeWater
import java.util.*

fun parseTypeToColor(type: Type): Color {
    return when(type.type.name.toLowerCase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when(stat.stat.name.toLowerCase()) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when(stat.stat.name.toLowerCase()) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}