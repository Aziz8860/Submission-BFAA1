package com.example.latihanfundamental.ui.follower

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.latihanfundamental.api.User
import com.example.latihanfundamental.databinding.ItemUserBinding
import com.example.latihanfundamental.ui.detail.DetailActivity

class FollowerAdapter(private val context: Context) :
    RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {

    private val userFollowerItems = ArrayList<User>()

    fun setAllData(data: List<User>) {
        userFollowerItems.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = userFollowerItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userFollowerItems[position])
    }

    class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                tvUsername.text = user.login
            }

            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.ivImageUser)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, user.login)
                itemView.context.startActivity(intent)
            }
        }
    }
}