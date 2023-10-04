package com.flexath.pokedex.data.repository

import com.flexath.pokedex.network.responses.PokedexListResponse
import com.flexath.pokedex.network.responses.PokedexResponse
import com.flexath.pokedex.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
interface PokedexRepository {

    suspend fun getPokedexList(
        limit: Int,
        offset: Int
    ) : Resource<PokedexListResponse>

    suspend fun getPokedexDetail(
        pokedexName: String
    ) : Resource<PokedexResponse>

}