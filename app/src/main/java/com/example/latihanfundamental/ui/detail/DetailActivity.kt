package com.example.latihanfundamental.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.latihanfundamental.R
import com.example.latihanfundamental.ui.viewpager.ViewPagerAdapter
import com.example.latihanfundamental.api.DetailUserResponse
import com.example.latihanfundamental.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val detailViewModel: DetailViewModel by viewModels()

    private var username: String? = null
    private var isDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        handleIntent()
        initToolbar()
        initPageAdapter()
        restoreState(savedInstanceState)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            isDataLoaded = savedInstanceState.getBoolean(STATE_IS_DATA_LOADED)
            username = savedInstanceState.getString(STATE_USERNAME)

            // Restore the DetailViewModel state
            username?.let {
                detailViewModel.listDetail.observe(this) { data ->
                    setStateFromAPI(data)
                }
            }
            detailViewModel.isLoading.observe(this) { isLoading ->
                showLoading(isLoading)
            }
        } else {
            // Load the data for the first time
            initObservers()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_IS_DATA_LOADED, isDataLoaded)
        outState.putString(STATE_USERNAME, username)
    }

    private fun handleIntent() {
        username = intent.getStringExtra(EXTRA_USER)
    }

    private fun initPageAdapter() {
        val viewPagerAdapter = ViewPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = viewPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun initToolbar() {
        supportActionBar?.apply {
            elevation = 0f
            title = username
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initObservers() {
        username?.let { detailViewModel.getDetailUser(it) }

        detailViewModel.listDetail.observe(this) { data ->
            isDataLoaded = true
            setStateFromAPI(data)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressDetail.visibility =
            if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun setStateFromAPI(data: DetailUserResponse) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(data.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imgAvatarDetail)

            tvName.text = data.name ?: data.login
            tvUsernameDetail.text = data.login
            tvCompany.text = data.company ?: getString(R.string.empty_state)
            tvLocation.text = data.location ?: getString(R.string.empty_state)
            tvRepositoryCount.text = data.publicRepos.toString()
            tvFollowerCount.text = data.followers.toString()
            tvFollowingCount.text = data.following.toString()

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun getUsernameString(): String? {
        return username
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
        const val EXTRA_USER = "EXTRA_USER"
        const val STATE_IS_DATA_LOADED = "STATE_IS_DATA_LOADED"
        const val STATE_USERNAME = "STATE_USERNAME"
    }
}