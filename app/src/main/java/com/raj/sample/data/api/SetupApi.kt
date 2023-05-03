package com.raj.sample.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SetupApi {

    @Multipart
    @POST("/upload-image")
    suspend fun uploadFile(@Part file: MultipartBody.Part): Call<ResponseBody>

    @Multipart
    @POST("/upload_image_progress")
    fun uploadImages(@Part filePart: MultipartBody.Part): Call<ResponseBody>

}