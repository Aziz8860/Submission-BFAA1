package com.example.latihanfundamental.ui.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihanfundamental.api.User
import com.example.latihanfundamental.databinding.FragmentFollowingBinding
import com.example.latihanfundamental.ui.detail.DetailActivity

class FollowingFragment : Fragment() {

    private val followingViewModel : FollowingViewModel by viewModels()

    private val binding : FragmentFollowingBinding by lazy {
        FragmentFollowingBinding.inflate(layoutInflater)
    }

    private val followingAdapter : FollowingAdapter by lazy {
        FollowingAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleUsername()
        initRecyclerView()

        // Check if there is saved state and update the UI accordingly
        if (savedInstanceState != null) {
            val followingList = savedInstanceState.getSerializable("followingList") as? List<User>
            followingList?.let {
                followingAdapter.setAllData(it)
            }

            val isLoading = savedInstanceState.getBoolean("isLoading")
            showLoading(isLoading)
        } else {
            initObservers()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable("followingList",
            followingViewModel.listFollowing.value?.let { ArrayList(it) })

        followingViewModel.isLoading.value?.let {
            outState.putBoolean("isLoading", it)
        }
    }

    private fun initRecyclerView() {
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = followingAdapter
        }
    }

    private fun initObservers() {
        followingViewModel.listFollowing.observe(viewLifecycleOwner) {
            followingAdapter.setAllData(it)
        }

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun handleUsername() {
        val activity = activity as DetailActivity
        val username: String? = activity.getUsernameString()
        username?.let { followingViewModel.getUserFollowing(it) }
    }

}