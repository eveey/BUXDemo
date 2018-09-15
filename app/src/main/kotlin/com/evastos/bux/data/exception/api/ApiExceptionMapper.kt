package com.evastos.bux.data.exception.api

import com.evastos.bux.data.exception.ExceptionMapper
import com.evastos.bux.data.exception.api.ApiException.AuthException
import com.evastos.bux.data.exception.api.ApiException.NetworkException
import com.evastos.bux.data.exception.api.ApiException.NotFoundException
import com.evastos.bux.data.exception.api.ApiException.ServerException
import com.evastos.bux.data.exception.api.ApiException.UnknownException
import com.evastos.bux.data.model.api.error.ApiError
import com.evastos.bux.data.model.api.error.ApiErrorCode.AUTH_007
import com.evastos.bux.data.model.api.error.ApiErrorCode.AUTH_008
import com.evastos.bux.data.model.api.error.ApiErrorCode.AUTH_009
import com.evastos.bux.data.model.api.error.ApiErrorCode.AUTH_014
import com.evastos.bux.data.model.api.error.ApiErrorCode.TRADING_002
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiExceptionMapper(private val moshi: Moshi) : ExceptionMapper<ApiException> {

    override fun map(throwable: Throwable): ApiException {
        var exception: ApiException = UnknownException()
        if (throwable is SocketTimeoutException || throwable is UnknownHostException) {
            exception = NetworkException()
        }
        if (throwable is ConnectException) {
            exception = ServerException()
        }
        if (throwable is HttpException) {
            if (throwable.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                exception = NotFoundException()
            } else {
                val responseBody = throwable.response().errorBody()?.string()
                responseBody?.let { errorResponse ->
                    val apiError = moshi.adapter(ApiError::class.java).fromJson(errorResponse)
                    exception = when (apiError?.errorCode) {
                        TRADING_002 -> ServerException()
                        AUTH_007, AUTH_014, AUTH_009, AUTH_008 -> AuthException()
                        else -> UnknownException()
                    }
                }
            }
        }
        return exception
    }
}