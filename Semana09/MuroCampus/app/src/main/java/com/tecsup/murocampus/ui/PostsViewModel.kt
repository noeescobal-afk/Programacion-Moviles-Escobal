package com.tecsup.murocampus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.data.mensajeDe
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {

    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista: StateFlow<UiState<List<Post>>> = _lista.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private var currentPostId: Int? = null

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _lista.value = UiState.Cargando
            repo.listar()
                .onSuccess { posts ->
                    _lista.value = UiState.Exito(posts)
                }
                .onFailure { error ->
                    _lista.value = UiState.Error(mensajeDe(error))
                }
        }
    }

    fun abrir(id: Int) {
        currentPostId = id
        viewModelScope.launch {
            _detalle.value = UiState.Cargando
            try {
                val resDetalle = repo.detalle(id)
                val resComentarios = repo.comentarios(id)

                if (resDetalle.isSuccess && resComentarios.isSuccess) {
                    val post = resDetalle.getOrNull()
                    val comentarios = resComentarios.getOrNull()
                    if (post != null && comentarios != null) {
                        _detalle.value = UiState.Exito(DetalleUi(post, comentarios))
                    } else {
                        _detalle.value = UiState.Error("Error desconocido")
                    }
                } else {
                    val error = resDetalle.exceptionOrNull()
                        ?: resComentarios.exceptionOrNull()
                        ?: Exception("Error desconocido")
                    _detalle.value = UiState.Error(mensajeDe(error))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _detalle.value = UiState.Error(mensajeDe(e))
            }
        }
    }

    fun reintentarDetalle() {
        currentPostId?.let { abrir(it) }
    }
}

class PostsViewModelFactory(private val repo: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
