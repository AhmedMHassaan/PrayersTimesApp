package com.ahmed.m.hassaan.prayerstimesapp.data.model.response

sealed class ApiResponse<DATA>(
    val isLoading: Boolean = true,
    val errorMsg: String? = null,
    val result: DATA? = null
) {
    class LoadingApiResponse<T>(isLoading: Boolean) :
        ApiResponse<T>(isLoading, errorMsg = null, result = null)

    class ErrorApiResponse<T>(errorMessage: String) :
        ApiResponse<T>(errorMsg = errorMessage, isLoading = false)

    class DataApiResponse<T>(data: T) : ApiResponse<T>(result = data, isLoading = false)
}
