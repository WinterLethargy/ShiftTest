package com.example.user.navigation
import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.fragment
import com.example.user.R
import com.example.user.fragments.UserDetailsFragment
import com.example.user.fragments.UsersFragment
import kotlinx.serialization.Serializable

@Serializable
object UsersRoute

@Serializable
internal data class UserDetailsRoute(val id: Long)

fun NavGraphBuilder.usersFragments(context: Context) {
    fragment<UsersFragment, UsersRoute> {
        label = context.getString(R.string.users_label)
    }
    fragment<UserDetailsFragment, UserDetailsRoute>()
}