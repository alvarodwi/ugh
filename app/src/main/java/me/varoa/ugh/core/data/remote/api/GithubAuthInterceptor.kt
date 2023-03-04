package me.varoa.ugh.core.data.remote.api

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class GithubAuthInterceptor(
    private val auth: String
) : Interceptor {
    override fun intercept(chain: Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Authorization", auth)
                .build()
        )
    }
}
