package com.example.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.name)

    fun bind(user: User?) {
        if(user == null)
            return

        name.text = "${user.name.title} ${user.name.first} ${user.name.last}"
    }

    companion object{
        fun create(parent: ViewGroup): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(com.example.user.R.layout.user_item, parent, false)
            return UserViewHolder(view)
        }
    }
}