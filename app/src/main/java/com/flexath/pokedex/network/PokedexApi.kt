package com.flexath.pokedex.network

import com.flexath.pokedex.network.responses.PokedexListResponse
import com.flexath.pokedex.network.responses.PokedexResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokedexApi {

    @GET(value = "pokemon")
    suspend fun getPokedexList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokedexListResponse

    @GET(value = "pokemon/{name}")
    suspend fun getPokedexDetail(
        @Path("name") pokedexName: String
    ): PokedexResponse
}