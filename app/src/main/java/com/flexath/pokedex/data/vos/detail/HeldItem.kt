package com.flexath.pokedex.data.vos.detail

data class HeldItem(
    val item: Item,
    val version_details: List<VersionDetail>
)