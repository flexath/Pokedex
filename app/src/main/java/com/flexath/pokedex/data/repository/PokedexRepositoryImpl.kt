package com.flexath.pokedex.data.repository

import com.flexath.pokedex.network.PokedexApi
import com.flexath.pokedex.network.responses.PokedexListResponse
import com.flexath.pokedex.network.responses.PokedexResponse
import com.flexath.pokedex.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.Exception
import javax.inject.Inject

@ActivityScoped
class PokedexRepositoryImpl @Inject constructor(
    private val pokedexApi: PokedexApi
) : PokedexRepository {
    override suspend fun getPokedexList(limit: Int, offset: Int): Resource<PokedexListResponse> {
        val response = try {
            pokedexApi.getPokedexList(limit,offset)
        } catch (e: Exception) {
            return Resource.Error(message = "No Internet Connection")
        }
        return Resource.Success(response)
    }

    override suspend fun getPokedexDetail(pokedexName: String): Resource<PokedexResponse> {
        val response = try {
            pokedexApi.getPokedexDetail(pokedexName)
        } catch (e: Exception) {
            return Resource.Error(message = "No Internet Connection")
        }
        return Resource.Success(response)
    }

}