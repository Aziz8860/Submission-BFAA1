package com.example.latihanfundamental.api

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getSearch(
        @Query("q") login: String
    ): Call<SearchResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") login: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getListFollowers(
        @Path("username") login: String
    ): Call<List<User>>

    @GET("users/{username}/following")
    fun getListFollowing(
        @Path("username") login: String
    ): Call<List<User>>
}