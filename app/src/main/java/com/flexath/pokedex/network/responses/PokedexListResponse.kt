package com.flexath.pokedex.network.responses

import com.flexath.pokedex.data.vos.home.Result

data class PokedexListResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)