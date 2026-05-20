package com.example.lab_10.rutinas.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab_10.rutinas.data.RutinasRepository
import com.example.lab_10.rutinas.model.RutinaModel
import kotlinx.coroutines.launch

class RutinasViewModel(private val repository: RutinasRepository) : ViewModel() {

    var listaRutinas by mutableStateOf<List<RutinaModel>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        cargarRutinas()
    }

    fun cargarRutinas() {
        viewModelScope.launch {
            isLoading = true
            try {
                listaRutinas = repository.obtenerRutinas()
            } catch (e: Exception) {
                // Manejar error en producción
            } finally {
                isLoading = false
            }
        }
    }

    fun eliminarRutina(id: Int) {
        viewModelScope.launch {
            val exito = repository.eliminarRutina(id)
            if (exito) {
                cargarRutinas() // Recargar lista tras borrar
            }
        }
    }
}