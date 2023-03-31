package com.example.latihanfundamental.ui.follower

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihanfundamental.api.User
import com.example.latihanfundamental.databinding.FragmentFollowersBinding
import com.example.latihanfundamental.ui.detail.DetailActivity

class FollowerFragment : Fragment() {

    private val followerViewModel : FollowerViewModel by viewModels()

    private val binding : FragmentFollowersBinding by lazy {
        FragmentFollowersBinding.inflate(layoutInflater)
    }

    private val followerAdapter : FollowerAdapter by lazy {
        FollowerAdapter(requireContext())
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
            val followerList = savedInstanceState.getSerializable("followerList") as? List<User>
            followerList?.let {
                followerAdapter.setAllData(it)
            }

            val isLoading = savedInstanceState.getBoolean("isLoading")
            showLoading(isLoading)
        } else {
            initObservers()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable("followerList",
            followerViewModel.listFollower.value?.let { ArrayList(it) })

        followerViewModel.isLoading.value?.let {
            outState.putBoolean("isLoading", it)
        }
    }

    private fun initRecyclerView() {
        binding.rvUser.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = followerAdapter
        }
    }

    private fun initObservers() {
        followerViewModel.listFollower.observe(viewLifecycleOwner) {
            followerAdapter.setAllData(it)
        }

        followerViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun handleUsername() {
        val activity = activity as DetailActivity
        val username: String? = activity.getUsernameString()
        username?.let { followerViewModel.getUserFollower(it) }
    }
}