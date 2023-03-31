package com.example.latihanfundamental.ui.following

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.latihanfundamental.api.ApiConfig
import com.example.latihanfundamental.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val _listFollowing = MutableLiveData<List<User>>()
    val listFollowing: LiveData<List<User>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "FollowingViewModel"
    }

    fun getUserFollowing(username: String) {
        _isLoading.postValue(true)
        val client = ApiConfig.getApiService().getListFollowing(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _listFollowing.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.postValue(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}