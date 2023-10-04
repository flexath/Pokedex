package com.flexath.pokedex.network.responses

import com.flexath.pokedex.data.vos.detail.Ability
import com.flexath.pokedex.data.vos.detail.Form
import com.flexath.pokedex.data.vos.detail.GameIndice
import com.flexath.pokedex.data.vos.detail.HeldItem
import com.flexath.pokedex.data.vos.detail.Move
import com.flexath.pokedex.data.vos.detail.Species
import com.flexath.pokedex.data.vos.detail.Sprites
import com.flexath.pokedex.data.vos.detail.Stat
import com.flexath.pokedex.data.vos.detail.Type

data class PokedexResponse(
    val abilities: List<Ability>,
    val base_experience: Int,
    val forms: List<Form>,
    val game_indices: List<GameIndice>,
    val height: Int,
    val held_items: List<HeldItem>,
    val id: Int,
    val is_default: Boolean,
    val location_area_encounters: String,
    val moves: List<Move>,
    val name: String,
    val order: Int,
    val past_types: List<Any>,
    val species: Species,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int
)