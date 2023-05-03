package com.raj.sample.repository

import com.raj.sample.util.Resource
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.io.File

interface MainRepository {

    suspend fun uploadImage(file: File): Resource<Unit>
   // suspend fun uploadImageProgress(file: MultipartBody.Part): Call<ResponseBody>
}