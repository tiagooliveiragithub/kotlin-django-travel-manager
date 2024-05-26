package online.tripguru.tripguruapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import online.tripguru.tripguruapp.network.auth.RequestResult
import online.tripguru.tripguruapp.repositories.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val signInResult = MutableLiveData<RequestResult<Unit>>()
    val signUpResult = MutableLiveData<RequestResult<Unit>>()

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            val result = repository.signIn(username, password)
            signInResult.postValue(result)
        }
    }

    fun signUp(username: String, firstname: String, lastname: String, email: String, password: String, confirmPassword: String) {
        // I want to confirm the password before sending the request
        if (password != confirmPassword) {
            signUpResult.postValue(RequestResult.Unauthorized())
            return
        }
        viewModelScope.launch {
            val result = repository.signUp(username, firstname, lastname, email, password)
            signUpResult.postValue(result)
        }
    }

}