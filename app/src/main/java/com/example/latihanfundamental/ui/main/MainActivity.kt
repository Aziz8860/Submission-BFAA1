package com.example.latihanfundamental.ui.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihanfundamental.R
import com.example.latihanfundamental.api.User
import com.example.latihanfundamental.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private var userItems: MutableList<User> = mutableListOf()

    private var username: String? = null
    private var isDataLoaded = false

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val mainAdapter: MainAdapter by lazy {
        MainAdapter(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)
        initRecyclerView()

        if (savedInstanceState != null) {
            isDataLoaded = savedInstanceState.getBoolean(STATE_IS_DATA_LOADED)
            username = savedInstanceState.getString(EXTRA_USERNAME)

            // Restore the MainViewModel state
            username?.let {
                mainViewModel.user.observe(this) { data ->
                    mainAdapter.setAllData(data)
                }
            }
            mainViewModel.isLoading.observe(this) { isLoading ->
                showLoading(isLoading)
            }
        } else {
            username = INITIAL_SEARCH
            initObservers()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        userItems.clear()
                        mainViewModel.getUsers(query)
                        searchView.clearFocus()
                    } else {
                        searchView.clearFocus()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    private fun initObservers() {
        username?.let { mainViewModel.getUsers(it) }

        mainViewModel.user.observe(this) {
            isDataLoaded = true
            mainAdapter.setAllData(it)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun initRecyclerView() {
        binding.rvUser.adapter = mainAdapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_IS_DATA_LOADED, isDataLoaded)
        outState.putString(EXTRA_USERNAME, username)
    }

    companion object {
        const val INITIAL_SEARCH = "jake"
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val STATE_IS_DATA_LOADED = "STATE_IS_DATA_LOADED"
    }
}