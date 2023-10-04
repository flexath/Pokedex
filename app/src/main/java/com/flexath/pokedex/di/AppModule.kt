package com.flexath.pokedex.di

import com.flexath.pokedex.data.repository.PokedexRepository
import com.flexath.pokedex.data.repository.PokedexRepositoryImpl
import com.flexath.pokedex.network.BASE_URL_POKEDEX
import com.flexath.pokedex.network.PokedexApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitApi(): PokedexApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_POKEDEX)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokedexApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(pokedexApi:PokedexApi) : PokedexRepository {
        return PokedexRepositoryImpl(pokedexApi)
    }
}