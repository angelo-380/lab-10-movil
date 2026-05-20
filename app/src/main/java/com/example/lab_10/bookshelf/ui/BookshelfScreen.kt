package com.example.lab_10.bookshelf.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.lab_10.bookshelf.data.NetworkBooksRepository
import com.example.lab_10.bookshelf.network.BooksApi
import kotlinx.coroutines.launch

sealed interface BooksUiState {
    data class Success(val thumbnails: List<String>) : BooksUiState
    object Error : BooksUiState
    object Loading : BooksUiState
}

class BooksViewModel : ViewModel() {
    var booksUiState: BooksUiState by mutableStateOf(BooksUiState.Loading)
        private set

    private val repository = NetworkBooksRepository(BooksApi.retrofitService)

    init {
        getBooks("jazz")
    }

    private fun getBooks(query: String) {
        viewModelScope.launch {
            booksUiState = BooksUiState.Loading
            try {
                val thumbnails = repository.getBookThumbnails(query)
                booksUiState = BooksUiState.Success(thumbnails)
            } catch (e: Exception) {
                android.util.Log.e("BOOK_ERROR", "Fallo total: ", e)
                booksUiState = BooksUiState.Error
            }
        }
    }
}

@Composable
fun BookshelfScreen(viewModel: BooksViewModel = viewModel()) {
    when (val state = viewModel.booksUiState) {
        is BooksUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is BooksUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(150.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(state.thumbnails) { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = "Portada del libro",
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .aspectRatio(0.7f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        is BooksUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error al conectarse a Google Books API.")
            }
        }
    }
}