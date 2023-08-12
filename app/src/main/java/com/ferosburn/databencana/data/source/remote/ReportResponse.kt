package com.ferosburn.databencana.data.source.remote

sealed class ReportResponse<out R> {
    data class Success<out T>(val data: T) : ReportResponse<T>()
    data class Error(val errorMessage: String) : ReportResponse<Nothing>()
    object Empty : ReportResponse<Nothing>()
}