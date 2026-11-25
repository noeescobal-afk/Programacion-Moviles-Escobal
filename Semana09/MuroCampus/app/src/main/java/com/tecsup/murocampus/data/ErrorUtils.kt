package com.tecsup.murocampus.data

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

fun mensajeDe(error: Throwable): String {
    return when (error) {
        is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
        is HttpException -> "Error del servidor (${error.code()})"
        is SerializationException -> "Respuesta inesperada"
        else -> "Error desconocido"
    }
}
