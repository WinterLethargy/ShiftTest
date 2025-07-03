package com.example.user.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.user.IUserRepository
import com.example.user.User
import com.example.user.navigation.UserDetailsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class UserDetailsViewModel  @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val usersRepository: IUserRepository,
) : ViewModel() {
    val route: UserDetailsRoute = savedStateHandle.toRoute()
    val uiState: StateFlow<UserDetailUIState> = usersRepository
        .getUser(route.id)
        .map { UserDetailUIState(it) }
        .catch { exception ->
            Log.e("UserDetailsViewModel", "error", exception)
            emit(UserDetailUIState.empty)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UserDetailUIState.empty
        )
}

data class UserDetailUIState(val user: User?){
    companion object{
        val empty = UserDetailUIState(null)
    }
}
