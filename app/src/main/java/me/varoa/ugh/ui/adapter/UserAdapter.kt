package me.varoa.ugh.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import me.varoa.ugh.R.string
import me.varoa.ugh.core.domain.model.User
import me.varoa.ugh.databinding.ItemUserBinding
import me.varoa.ugh.ui.adapter.UserAdapter.UserViewHolder
import me.varoa.ugh.ui.ext.viewBinding

class UserAdapter(
    private val imageLoader: ImageLoader,
    private val onClick: (User) -> Unit
) : PagingDataAdapter<User, UserViewHolder>(USER_DIFF) {
    companion object {
        val USER_DIFF = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean = oldItem == newItem
        }
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User?) {
            if (data == null) return
            with(binding) {
                root.setOnClickListener { onClick(data) }
                ivAvatar.apply {
                    val imgData = ImageRequest.Builder(this.context)
                        .data(data.avatar)
                        .target(this)
                        .transformations(CircleCropTransformation())
                        .allowHardware(true)
                        .build()
                    imageLoader.enqueue(imgData)
                }
                tvUsername.text = root.context.getString(string.label_username, data.username)
            }
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(parent.viewBinding(ItemUserBinding::inflate))
}
