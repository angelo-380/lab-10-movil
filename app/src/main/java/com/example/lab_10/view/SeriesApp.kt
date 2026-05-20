package com.example.lab_10.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab_10.data.SerieApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.lab_10.bookshelf.ui.BookshelfScreen
import com.example.lab_10.rutinas.network.RutinasApiService
import com.example.lab_10.rutinas.data.RutinasRepository
import com.example.lab_10.rutinas.ui.RutinasViewModel
import com.example.lab_10.rutinas.ui.RutinasListadoScreen
import com.example.lab_10.rutinas.ui.RutinaFormularioScreen
import androidx.compose.runtime.remember

@Composable
fun SeriesApp() {
    val urlBase = "http://161.132.49.139:8004/"
    val retrofit = Retrofit.Builder().baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create()).build()

    val servicio = retrofit.create(SerieApiService::class.java)

    val apiRutinas = retrofit.create(RutinasApiService::class.java)
    val repositorioRutinas = remember { RutinasRepository(apiRutinas) }
    val viewModelRutinas = remember { RutinasViewModel(repositorioRutinas) }

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.padding(top = 40.dp),
        topBar =    { BarraSuperior() },
        bottomBar = { BarraInferior(navController) },
        floatingActionButton = { BotonFAB(navController, servicio, viewModelRutinas) },
        content =   { paddingValues -> Contenido(paddingValues, navController, servicio, repositorioRutinas, viewModelRutinas) }
    )
}

@Composable
fun BotonFAB(navController: NavHostController, servicio: SerieApiService, viewModelRutinas: RutinasViewModel) {
    val cbeState by navController.currentBackStackEntryAsState()
    val rutaActual = cbeState?.destination?.route
    if (rutaActual == "series") {
        FloatingActionButton(
            containerColor = Color(0xFF2196F3),
            contentColor = Color.White,
            onClick = { navController.navigate("serieNuevo") }
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add"
            )
        }
    } else if (rutaActual == "rutinas") {
        FloatingActionButton(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White,
            // AQUÍ LLAMA AL MISMO NOMBRE
            onClick = { navController.navigate("rutinaEditar/0") }
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "SERIES APP",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF2196F3)
        )
    )
}

@Composable
fun BarraInferior(navController: NavHostController) {
    NavigationBar(
        containerColor = Color.LightGray
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = navController.currentDestination?.route == "inicio",
            onClick = { navController.navigate("inicio") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Favorite, contentDescription = "Series") },
            label = { Text("Series") },
            selected = navController.currentDestination?.route == "series",
            onClick = { navController.navigate("series") }
        )
    }
}

@Composable
fun Contenido(
    pv: PaddingValues,
    navController: NavHostController,
    servicio: SerieApiService,
    reposioRutinas: RutinasRepository,
    viewModelRutinas: RutinasViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
    ) {
        NavHost(
            navController = navController,
            startDestination = "inicio"
        ) {
            composable("inicio") { ScreenInicio(navController) }
            composable("series") { ContenidoSeriesListado(navController, servicio) }
            composable("bookshelf") { BookshelfScreen() }

            composable("rutinas") {
                RutinasListadoScreen(navController, viewModelRutinas)
            }

            // AQUI ESTABA EL ERROR: Cambié "rutinaFormulario/{id}" por "rutinaEditar/{id}"
            composable(
                route = "rutinaEditar/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                RutinaFormularioScreen(
                    navController = navController,
                    repository = reposioRutinas,
                    viewModel = viewModelRutinas,
                    rutinaId = id
                )
            }

            composable("serieNuevo") {
                ContenidoSerieEditar(navController, servicio, 0)
            }
            composable("serieVer/{id}", arguments = listOf(
                navArgument("id") { type = NavType.IntType } )
            ) {
                ContenidoSerieEditar(navController, servicio, it.arguments!!.getInt("id"))
            }
            composable("serieDel/{id}", arguments = listOf(
                navArgument("id") { type = NavType.IntType } )
            ) {
                ContenidoSerieEliminar(navController, servicio, it.arguments!!.getInt("id"))
            }
        }
    }
}

@Composable
fun ScreenInicio(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = { navController.navigate("series") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Icon(imageVector = Icons.Outlined.Favorite, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Ver Catálogo de Series", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("bookshelf") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE64A19))
        ) {
            Icon(imageVector = Icons.Outlined.Build, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Proyecto Bookshelf", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("rutinas") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Agenda de Rutinas TEA", fontSize = 16.sp)
        }
    }
}