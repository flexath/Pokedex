package com.flexath.pokedex.ui.view_models

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.flexath.pokedex.data.repository.PokedexRepository
import com.flexath.pokedex.network.PAGE_SIZE
import com.flexath.pokedex.ui.models.PokedexEntryItem
import com.flexath.pokedex.utils.LiveNetworkChecker
import com.flexath.pokedex.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokedexListViewModel @Inject constructor(
    private val repository: PokedexRepository
) : ViewModel() {

    private var currentPage = 0

    var pokedexList = mutableStateOf<List<PokedexEntryItem>>(listOf())
    var isLoading = mutableStateOf(false)
    var loadError = mutableStateOf("")
    var endReached = mutableStateOf(false)

    private var cachedPokedexList = listOf<PokedexEntryItem>()
    var isSearching = mutableStateOf(false)
    var isSearchStarting = true

    init {
        loadPokedexListPaginated()
    }

    fun calculateDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

    fun loadPokedexListPaginated() {
        isLoading.value = true
        viewModelScope.launch {
            val result = repository.getPokedexList(PAGE_SIZE, currentPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    endReached.value = (currentPage * PAGE_SIZE) >= (result.data?.count ?: 0)
                    val pokedexItemList = result.data?.results?.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith('/')) {
                            entry.url.dropLast(1).takeLastWhile {
                                it.isDigit()
                            }
                        } else {
                            entry.url.takeLastWhile {
                                it.isDigit()
                            }
                        }
                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$number.png"
                        PokedexEntryItem(pokedexName = entry.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }, imageUrl = url, number = number.toInt())
                    }

                    currentPage++
                    isLoading.value = false
                    loadError.value = ""
                    pokedexList.value += pokedexItemList ?: listOf()
                }

                is Resource.Error -> {
                    isLoading.value = false
                    loadError.value = result.message ?: ""
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    fun searchPokedex(query: String) {

        val listToSearch = if (isSearchStarting) {
            pokedexList.value
        } else {
            cachedPokedexList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokedexList.value = cachedPokedexList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            } else {
                val results = listToSearch.filter {
                    it.pokedexName.contains(
                        query.trim(),
                        ignoreCase = true
                    ) || it.number.toString() == query.trim()
                }

                if (isSearchStarting) {
                    cachedPokedexList = pokedexList.value
                    isSearchStarting = false
                }

                pokedexList.value = results
                isSearching.value = true
            }
        }
    }
}