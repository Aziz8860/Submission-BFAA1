package com.example.latihanfundamental.ui.follower

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.latihanfundamental.api.ApiConfig
import com.example.latihanfundamental.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel : ViewModel() {
    private val _listFollower = MutableLiveData<List<User>>()
    val listFollower: LiveData<List<User>> = _listFollower

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FollowerViewModel"
    }

    fun getUserFollower(username: String) : LiveData<List<User>> {
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().getListFollowers(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _listFollower.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
        return listFollower
    }
}