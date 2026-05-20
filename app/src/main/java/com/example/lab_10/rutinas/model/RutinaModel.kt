package com.example.lab_10.rutinas.model

import com.google.gson.annotations.SerializedName

data class RutinaModel(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("hora") val hora: String,
    @SerializedName("arasaacId") val arasaacId: Int // ID del pictograma de ARASAAC
)
