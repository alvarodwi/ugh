package me.varoa.ugh.core.data.remote

import logcat.logcat
import me.varoa.ugh.utils.ApiException
import org.json.JSONException
import retrofit2.Response

open class SafeApiRequest {
    suspend fun <T : Any> apiRequest(
        call: suspend () -> Response<T>,
        decodeErrorJson: suspend (String) -> String
    ): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            logcat { "Response is " + response.body().toString() }
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let {
                try {
                    message.append(decodeErrorJson(it))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            throw ApiException(message.toString())
        }
    }
}
