package com.flexath.pokedex.ui.view_models

import androidx.lifecycle.ViewModel
import com.flexath.pokedex.data.repository.PokedexRepository
import com.flexath.pokedex.network.responses.PokedexResponse
import com.flexath.pokedex.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokedexDetailViewModel @Inject constructor(
    private val repository: PokedexRepository
) : ViewModel() {

    suspend fun getPokedexInfo(pokedexName: String): Resource<PokedexResponse> {
        return repository.getPokedexDetail(pokedexName = pokedexName)
    }
}