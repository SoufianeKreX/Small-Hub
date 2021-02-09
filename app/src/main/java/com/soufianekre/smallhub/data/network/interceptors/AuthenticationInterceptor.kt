package com.soufianekre.smallhub.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class AuthenticationInterceptor : Interceptor {
    private var isScrapping: Boolean = false
    private var token: String? = null
    private var otp: String? = null

    constructor(token: String? = null, otp: String? = null) {
        this.token = token
        this.otp = otp
    }

    @JvmOverloads constructor(isScrapping: Boolean = false) {
        this.isScrapping = isScrapping
    }

    @Throws(IOException::class) override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        if (!isScrapping) builder.addHeader("User-Agent", "FastHub")
        val request = builder.build()
        return chain.proceed(request)
    }
}