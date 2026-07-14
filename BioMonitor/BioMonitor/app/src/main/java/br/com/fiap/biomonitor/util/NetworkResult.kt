package br.com.fiap.biomonitor.util

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

sealed class NetworkResult<out T> {


    data class Success<T>(val data: T) : NetworkResult<T>()


    data class Error<T>(
        val message: String,
        val code: Int? = null
    ) : NetworkResult<T>()


    class Loading<T> : NetworkResult<T>()


    val isSuccess: Boolean get() = this is Success


    val isError: Boolean get() = this is Error


    val isLoading: Boolean get() = this is Loading


    fun getOrNull(): T? = (this as? Success)?.data


    fun errorOrNull(): String? = (this as? Error)?.message


    inline fun <R> map(transform: (T) -> R): NetworkResult<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> Error(message, code)
            is Loading -> Loading()
        }
    }


    inline fun onSuccess(action: (T) -> Unit): NetworkResult<T> {
        if (this is Success) action(data)
        return this
    }


    inline fun onError(action: (String, Int?) -> Unit): NetworkResult<T> {
        if (this is Error) action(message, code)
        return this
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                NetworkResult.Success(it)
            } ?: NetworkResult.Error(ErrorMessages.EMPTY_RESPONSE)
        } else {
            NetworkResult.Error(
                message = "Erro: ${response.code()}",
                code = response.code()
            )
        }
    } catch (e: SocketTimeoutException) {
        NetworkResult.Error(ErrorMessages.TIMEOUT_ERROR)
    } catch (e: IOException) {
        NetworkResult.Error(ErrorMessages.NO_INTERNET)
    } catch (e: HttpException) {
        NetworkResult.Error(
            message = "${ErrorMessages.SERVER_ERROR}: ${e.code()}",
            code = e.code()
        )
    } catch (e: Exception) {
        NetworkResult.Error("${ErrorMessages.UNKNOWN_ERROR}: ${e.message}")
    }
}

suspend fun <T> safeDatabaseCall(dbCall: suspend () -> T): Result<T> {
    return try {
        Result.success(dbCall())
    } catch (e: android.database.sqlite.SQLiteConstraintException) {
        Result.failure(Exception(ErrorMessages.DUPLICATE_DATA))
    } catch (e: android.database.sqlite.SQLiteException) {
        Result.failure(Exception(ErrorMessages.DATABASE_ERROR))
    } catch (e: Exception) {
        Result.failure(Exception("${ErrorMessages.UNKNOWN_ERROR}: ${e.message}"))
    }
}
