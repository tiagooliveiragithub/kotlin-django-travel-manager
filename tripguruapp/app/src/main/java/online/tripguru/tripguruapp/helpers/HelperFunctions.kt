package online.tripguru.tripguruapp.helpers

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import online.tripguru.tripguruapp.localstorage.models.Local
import online.tripguru.tripguruapp.localstorage.models.Trip
import online.tripguru.tripguruapp.network.LocalResponse
import online.tripguru.tripguruapp.network.SignupFormRequest
import online.tripguru.tripguruapp.network.TripResponse
import java.io.File

fun convertResponseToTrip(tripResponse: TripResponse): Trip {
    return Trip(
        id = tripResponse.id,
        name = tripResponse.name,
        description = tripResponse.description,
        startDate = tripResponse.start_date,
        endDate = tripResponse.end_date,
        image = tripResponse.image,
        owners = tripResponse.users.joinToString(", ") { it.username }
    )
}

fun convertResponseToLocal(localResponse: LocalResponse): Local {
    return Local(
        id = localResponse.id,
        tripId = localResponse.tripId,
        name = localResponse.name,
        description = localResponse.description,
        latitude = localResponse.latitude,
        longitude = localResponse.longitude,
        address = localResponse.address,
        images = localResponse.photos
    )
}

fun convertToSingUpForm(context: Context, username: String, firstname: String, lastname: String, email: String, password: String, avatar: Uri): SignupFormRequest {
    val usernamePart = createPartFromString(username)
    val firstnamePart = createPartFromString(firstname)
    val lastnamePart = createPartFromString(lastname)
    val emailPart = createPartFromString(email)
    val passwordPart = createPartFromString(password)
    val avatarPart = getFileFromUri(context, avatar, "avatar")
    return SignupFormRequest(usernamePart, firstnamePart, lastnamePart, emailPart, passwordPart, avatarPart)
}

fun getFileFromUri(context: Context, uri: Uri, name: String): MultipartBody.Part {
    val filesDir = context.filesDir
    val file = File(filesDir, "image.jpg")
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = file.outputStream()

    inputStream.use { input ->
        outputStream.use { output ->
            input?.copyTo(output)
        }
    }

    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(name, file.name, requestBody)
}

fun createPartFromString(value: String): RequestBody {
    return value.toRequestBody("text/plain".toMediaTypeOrNull())
}
