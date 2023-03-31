package com.example.latihanfundamental.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.example.latihanfundamental.api.ApiConfig
import com.example.latihanfundamental.api.SearchResponse
import com.example.latihanfundamental.api.User
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _user = MutableLiveData<List<User>>()
    val user: LiveData<List<User>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun getUsers(username: String) {
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().getSearch(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _user.postValue(response.body()?.items)
                } else {
                    Log.e(TAG, "onFailure: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}