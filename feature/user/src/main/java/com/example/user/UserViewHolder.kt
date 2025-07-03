package com.example.user

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserViewHolder(
    view: View,
    val tapAction: (Long) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val image: ImageView = view.findViewById(R.id.image)
    private val name: TextView = view.findViewById(R.id.name)
    private val phone: TextView = view.findViewById(R.id.phone)

    init {
        itemView.setOnClickListener {
            user?.let {
                tapAction(it.id)
            }
        }

        phone.setOnClickListener{
            val phone = user?.phone ?: return@setOnClickListener
            val phoneUrl = Uri.parse("tel:$phone")
            val dialIntent = Intent(Intent.ACTION_DIAL,phoneUrl)
            try {
                itemView.context.startActivity(dialIntent)
            }
            catch (ex: ActivityNotFoundException){
                Log.e("UserDetailsFragment", ex.toString())
            }
        }
    }


    var user: User? = null
        private set
        get

    fun bind(user: User) {
        this.user = user

        name.text = "${user.name.title} ${user.name.first} ${user.name.last}"
        phone.text = user.phone
        Glide.with(itemView).clear(image)
        Glide.with(itemView)
            .load(user.picture.medium)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(image)
    }

    companion object{
        fun create(
            parent: ViewGroup,
            tapAction: (Long) -> Unit
        ): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(com.example.user.R.layout.user_item, parent, false)
            return UserViewHolder(view, tapAction)
        }
    }
}