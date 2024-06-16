package online.tripguru.tripguruapp.viewmodels

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.helpers.convertToSingUpForm
import online.tripguru.tripguruapp.network.EditUserRequest
import online.tripguru.tripguruapp.network.EditUserResponse
import online.tripguru.tripguruapp.network.GetUserInfoResponse
import online.tripguru.tripguruapp.network.LoginRequest
import online.tripguru.tripguruapp.network.LoginResponse
import online.tripguru.tripguruapp.network.Resource
import online.tripguru.tripguruapp.network.SignupResponse
import online.tripguru.tripguruapp.repositories.UserRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    val resultSignIn = MutableLiveData<Resource<LoginResponse>>()
    val resultSignUp = MutableLiveData<Resource<SignupResponse>>()
    val resultEditProfile = MutableLiveData<Resource<EditUserResponse>>()
    val resultGetUserInfo = MutableLiveData<Resource<GetUserInfoResponse>>()

    fun signIn(username: String, password: String) {
        if(isOnline().value == false) {
            Toast.makeText(context, R.string.nointernet_label, Toast.LENGTH_SHORT).show()
            return
        }
        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        }

        resultSignIn.postValue(Resource.loading())
        viewModelScope.launch {
            val result = userRepository.signIn(LoginRequest(username, password))
            resultSignIn.postValue(result)
        }
    }

    fun signUp(username: String, firstname: String, lastname: String, email: String, password: String, confirmPassword: String, avatar: Uri?) {
        if(username.isEmpty() || firstname.isEmpty() || lastname.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        } else if(email.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        } else if(password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        } else if(password != confirmPassword) {
            Toast.makeText(context, R.string.passwordnotidentical_label, Toast.LENGTH_SHORT).show()
            return
        } else if(avatar == null) {
            Toast.makeText(context, R.string.noavatar_label, Toast.LENGTH_SHORT).show()
            return
        }

        resultSignUp.postValue(Resource.loading())
        viewModelScope.launch {
            val signUpFormRequest = convertToSingUpForm(context, username, firstname, lastname, email, password, avatar)
            val result = userRepository.signUp(signUpFormRequest)
            resultSignUp.postValue(result)
        }
    }

    fun editUser(firstname: String, lastname: String, email: String, password: String, confirmPassword: String) {
        if(firstname.isEmpty() || lastname.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        } else if (email.isEmpty()) {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        } else if (password.isEmpty() || confirmPassword.isEmpty())  {
            Toast.makeText(context, R.string.emptyfields_label, Toast.LENGTH_SHORT).show()
            return
        } else if (password != confirmPassword) {
            Toast.makeText(context, R.string.passwordnotidentical_label, Toast.LENGTH_SHORT).show()
            return
        }

        resultEditProfile.postValue(Resource.loading())
        viewModelScope.launch {
            val result = userRepository.editUser(EditUserRequest(firstname, lastname, email, password))
            resultEditProfile.postValue(result)
        }
    }

    fun getUserInfo() {
        resultGetUserInfo.postValue(Resource.loading())
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

    fun autoSignIn() {
        resultSignIn.postValue(Resource.loading())
        viewModelScope.launch {
            val result = userRepository.isAuthorizedVerify()
            resultSignIn.postValue(result)
        }
    }

    fun getUserOfflineDetails() : String {
        return userRepository.getUserOfflineDetails()
    }


}