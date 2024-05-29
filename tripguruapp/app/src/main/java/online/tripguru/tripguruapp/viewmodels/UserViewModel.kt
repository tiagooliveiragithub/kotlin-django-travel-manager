package online.tripguru.tripguruapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import online.tripguru.tripguruapp.network.auth.RequestResult
import online.tripguru.tripguruapp.repositories.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val signInResult = MutableLiveData<RequestResult<Unit>>()
    val signUpResult = MutableLiveData<RequestResult<Unit>>()

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.signIn(username, password)
            signInResult.postValue(result)
        }
    }

    fun signUp(username: String, firstname: String, lastname: String, email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            signUpResult.postValue(RequestResult.Unauthorized())
            return
        }
        viewModelScope.launch {
            val result = userRepository.signUp(username, firstname, lastname, email, password)
            signUpResult.postValue(result)
        }
    }

    fun signOut() {
        userRepository.signOut()
    }

    fun isOnline() : LiveData<Boolean> {
        return userRepository.isOnline()
    }

    fun isAuthorized() : LiveData<Boolean> {
        return userRepository.isAuthorized()
    }

    fun isSignedIn() : LiveData<Boolean> {
        viewModelScope.launch {
            userRepository.isAuthorizedVerify()
        }
        return userRepository.isAuthorized()
    }
}