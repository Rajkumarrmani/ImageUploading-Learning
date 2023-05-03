package com.raj.sample.repository

import android.content.Context
import com.raj.sample.R
import com.raj.sample.data.api.SetupApi
import com.raj.sample.util.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val setupApi: SetupApi, private val context: Context
) : MainRepository {

    override suspend fun uploadImage(file: File): Resource<Unit> {
        val response = try {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            setupApi.uploadFile(body)
        } catch (e: HttpException) {
            return Resource.Error(context.getString(R.string.error_http))
        } catch (e: IOException) {
            return Resource.Error(context.getString(R.string.check_internet_connection))
        }

        return if (response.isExecuted) {
            Resource.Success(Unit)
        } else {
            Resource.Error(context.getString(R.string.error_unknown))
        }
    }



}