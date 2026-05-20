package com.example.lab_10.rutinas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab_10.rutinas.data.RutinasRepository
import com.example.lab_10.rutinas.model.RutinaModel
import kotlinx.coroutines.launch

@Composable
fun RutinaFormularioScreen(
    navController: NavController,
    repository: RutinasRepository,
    viewModel: RutinasViewModel,
    rutinaId: Int = 0
) {
    val coroutineScope = rememberCoroutineScope()

    // Inicializamos las variables de estado vacías para la creación
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var arasaacId by remember { mutableStateOf("") }

    // OBLIGATORIO: Si es edición (rutinaId > 0), buscamos la rutina en la lista del ViewModel y rellenamos los campos
    LaunchedEffect(rutinaId) {
        if (rutinaId != 0) {
            // Buscamos la rutina que coincida con el ID dentro de la lista actual del ViewModel
            val rutinaExistente = viewModel.listaRutinas.find { it.id == rutinaId }
            if (rutinaExistente != null) {
                titulo = rutinaExistente.titulo
                descripcion = rutinaExistente.descripcion
                hora = rutinaExistente.hora
                arasaacId = rutinaExistente.arasaacId.toString()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Actividad (Ej. Lavarse las manos)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción de la rutina") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (Ej. 08:00 AM)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = arasaacId,
            onValueChange = { arasaacId = it },
            label = { Text("ID del Pictograma ARASAAC") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val rutinaData = RutinaModel(
                        id = rutinaId,
                        titulo = titulo,
                        descripcion = descripcion,
                        hora = hora,
                        arasaacId = arasaacId.toIntOrNull() ?: 0
                    )

                    if (rutinaId == 0) {
                        repository.crearRutina(rutinaData)
                    } else {
                        repository.actualizarRutina(rutinaId, rutinaData)
                    }
                    viewModel.cargarRutinas()
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (rutinaId == 0) "Guardar Nueva Rutina" else "Actualizar Rutina")
        }
    }
}