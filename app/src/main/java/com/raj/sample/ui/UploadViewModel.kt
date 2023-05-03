package com.raj.sample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raj.sample.data.api.SetupApi
import com.raj.sample.online.ProgressListener
import com.raj.sample.online.ProgressRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val setupApi: SetupApi
) : ViewModel() {

    fun uploadFile(filePath: String, progressListener: ProgressListener): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val file = File(filePath)
        val requestBody = ProgressRequestBody(
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file),
            progressListener
        )
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val call = setupApi.uploadImages(filePart)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                result.postValue(true)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                result.postValue(false)
            }
        })
        return result

    }
}



    /* call.enqueue(object : Callback<ResponseBody> {
         override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
             result.postValue(true)
         }

         override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
             result.postValue(false)
         }
     })
     return result*/
