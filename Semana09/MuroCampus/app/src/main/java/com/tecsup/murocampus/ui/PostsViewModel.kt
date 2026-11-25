package com.tecsup.murocampus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.data.mensajeDe
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
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

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle: StateFlow<UiState<DetalleUi>> = _detalle.asStateFlow()

    private val _publicar = MutableStateFlow<UiState<Post>?>(null)
    val publicar: StateFlow<UiState<Post>?> = _publicar.asStateFlow()

    private var postsOriginales: List<Post> = emptyList()
    private var currentPostId: Int? = null
    private var cargarJob: Job? = null

    init {
        cargar()
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun actualizarBusqueda(query: String) {
        _busqueda.value = query
        if (_lista.value is UiState.Exito) {
            val trimmed = query.trim()
            val filtrados = if (trimmed.isEmpty()) {
                postsOriginales
            } else {
                postsOriginales.filter { it.title.contains(trimmed, ignoreCase = true) }
            }
            _lista.value = UiState.Exito(filtrados)
        }
    }

    fun cargar() {
        cargarJob?.cancel()
        cargarJob = viewModelScope.launch {
            _lista.value = UiState.Cargando
            val userId = _usuarioFiltro.value
            val resultado = if (userId == null) {
                repo.listar()
            } else {
                repo.porUsuario(userId)
            }

            resultado
                .onSuccess { posts ->
                    postsOriginales = posts
                    val query = _busqueda.value.trim()
                    val filtrados = if (query.isEmpty()) {
                        postsOriginales
                    } else {
                        postsOriginales.filter { it.title.contains(query, ignoreCase = true) }
                    }
                    _lista.value = UiState.Exito(filtrados)
                }
                .onFailure { error ->
                    _lista.value = UiState.Error(mensajeDe(error))
                }
        }
    }

    fun publicar(titulo: String, texto: String) {
        if (_publicar.value is UiState.Cargando) return
        viewModelScope.launch {
            _publicar.value = UiState.Cargando
            repo.crear(NuevoPost(userId = 1, title = titulo, body = texto))
                .onSuccess { postCreado ->
                    _publicar.value = UiState.Exito(postCreado)
                    cargar()
                }
                .onFailure { error ->
                    _publicar.value = UiState.Error(mensajeDe(error))
                }
        }
    }

    fun limpiarPublicar() {
        _publicar.value = null
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
