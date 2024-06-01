package online.tripguru.tripguruapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import online.tripguru.tripguruapp.network.EditUserRequest
import online.tripguru.tripguruapp.network.EditUserResponse
import online.tripguru.tripguruapp.network.GetUserInfoResponse
import online.tripguru.tripguruapp.network.LoginRequest
import online.tripguru.tripguruapp.network.LoginResponse
import online.tripguru.tripguruapp.network.SignupRequest
import online.tripguru.tripguruapp.network.SignupResponse
import online.tripguru.tripguruapp.network.Resource
import online.tripguru.tripguruapp.repositories.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val resultSignIn = MutableLiveData<Resource<LoginResponse>>()
    val resultSignUp = MutableLiveData<Resource<SignupResponse>>()
    val resultEditProfile = MutableLiveData<Resource<EditUserResponse>>()
    val resultGetUserInfo = MutableLiveData<Resource<GetUserInfoResponse>>()

    fun signIn(username: String, password: String) {
        resultSignIn.postValue(Resource.loading(null))
        if(username.isEmpty() || password.isEmpty()) {
            resultSignIn.postValue(Resource.error("Username and password cannot be empty", null))
            return
        }
        viewModelScope.launch {
            val result = userRepository.signIn(LoginRequest(username, password))
            resultSignIn.postValue(result)
        }
    }

    fun signUp(username: String, firstname: String, lastname: String, email: String, password: String, confirmPassword: String) {
        resultSignUp.postValue(Resource.loading(null))
        if(username.isEmpty() || firstname.isEmpty() || lastname.isEmpty()) {
            resultSignUp.postValue(Resource.error("Username, first name and last name cannot be empty", null))
            return
        } else if(email.isEmpty()) {
            resultSignUp.postValue(Resource.error("Email cannot be empty", null))
            return
        } else if(password.isEmpty() || confirmPassword.isEmpty()) {
            resultSignUp.postValue(Resource.error("Password cannot be empty", null))
            return
        } else if(password != confirmPassword) {
            resultSignUp.postValue(Resource.error("Passwords do not match", null))
            return
        }
        viewModelScope.launch {
            val result = userRepository.signUp(SignupRequest(username, firstname, lastname, email, password))
            resultSignUp.postValue(result)
        }
    }

    fun editUser(firstname: String, lastname: String, email: String, password: String, confirmPassword: String) {
        resultEditProfile.postValue(Resource.loading(null))
        if(firstname.isEmpty() || lastname.isEmpty()) {
            resultEditProfile.postValue(Resource.error("First name and last name cannot be empty", null))
            return
        } else if (email.isEmpty()) {
            resultEditProfile.postValue(Resource.error("Email cannot be empty", null))
            return
        } else if (password.isEmpty() || confirmPassword.isEmpty())  {
            resultEditProfile.postValue(Resource.error("Password cannot be empty", null))
            return
        } else if (password != confirmPassword) {
            resultEditProfile.postValue(Resource.error("Passwords do not match", null))
            return
        }
        viewModelScope.launch {
            val result = userRepository.editUser(EditUserRequest(firstname, lastname, email, password))
            resultEditProfile.postValue(result)
        }
    }

    fun getUserInfo() {
        resultGetUserInfo.postValue(Resource.loading(null))
        viewModelScope.launch {
            val result = userRepository.getUserInfo()
            resultGetUserInfo.postValue(result)
        }
    }

    fun signOut() {
        userRepository.signOut()
    }

    fun isOnline() : LiveData<Boolean> {
        return userRepository.isOnline()
    }

    fun isSignedIn() : LiveData<Boolean> {
        viewModelScope.launch {
            userRepository.isAuthorizedVerify()
        }
        return userRepository.isSignedIn
    }
}