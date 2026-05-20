package com.example.lab_10.rutinas.data

import com.example.lab_10.rutinas.model.RutinaModel
import com.example.lab_10.rutinas.network.RutinasApiService

class RutinasRepository(private val apiService: RutinasApiService) {

    suspend fun obtenerRutinas(): List<RutinaModel> {
        return apiService.selectRutinas()
    }

    suspend fun crearRutina(rutina: RutinaModel): Boolean {
        val response = apiService.insertRutina(rutina)
        return response.isSuccessful
    }

    suspend fun actualizarRutina(id: Int, rutina: RutinaModel): Boolean {
        val response = apiService.updateRutina(id, rutina)
        return response.isSuccessful
    }

    suspend fun eliminarRutina(id: Int): Boolean {
        val response = apiService.deleteRutina(id)
        return response.isSuccessful
    }
}