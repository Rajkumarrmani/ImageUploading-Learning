package com.raj.sample.online

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.IOException
import okio.Sink
import okio.buffer

class ProgressRequestBody(private val requestBody: RequestBody, private val listener: ProgressListener) : RequestBody() {

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    override fun contentLength(): Long {
        return try {
            requestBody.contentLength()
        } catch (e: IOException) {
            -1
        }
    }

    override fun writeTo(sink: BufferedSink) {
        val countingSink = CountingSink(sink)
        val bufferedSink = countingSink.buffer()
        requestBody.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {

        private var bytesWritten = 0L
        private val contentLength = contentLength()

        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            bytesWritten += byteCount
            listener.onProgress((100 * bytesWritten / contentLength).toInt())
        }
    }
}