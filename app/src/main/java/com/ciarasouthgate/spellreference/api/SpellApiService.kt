package com.ciarasouthgate.spellreference.api

import com.ciarasouthgate.spellreference.database.Spell
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://www.dnd5eapi.co/api/"

private val moshi = Moshi.Builder()
    .add(SpellAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface SpellApiService {
    @GET("spells")
    suspend fun getSpells(
        @Query("level") levels: String?
    ): SpellListResponse

    @GET("spells/{index}")
    suspend fun getSpell(@Path("index") index: String): Spell
}

object SpellApi {
    private val service: SpellApiService by lazy {
        retrofit.create(SpellApiService::class.java)
    }

    suspend fun getSpells(levels: List<Int>? = null): List<Spell> =
        service.getSpells(levels?.joinToString(",")).results

    suspend fun getSpell(index: String): Spell =
        service.getSpell(index)
}