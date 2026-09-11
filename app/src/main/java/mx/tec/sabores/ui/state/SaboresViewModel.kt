
package mx.tec.sabores.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.tec.sabores.data.RestaurantRepository
import mx.tec.sabores.domain.RatingSummary
import mx.tec.sabores.domain.Restaurant
import mx.tec.sabores.domain.RestaurantEnLista
import mx.tec.sabores.domain.Review
import okio.IOException
import retrofit2.HttpException

data class MyReviewItem(val restaurantName: String, val review: Review)

/** El restaurante y sus reseñas, que la pantalla de detalle necesita juntos. */
data class Detalle(
    val restaurant: Restaurant,
    val reviews: List<Review>
) {
    val summary: RatingSummary = RatingSummary.from(reviews)
}

class SaboresViewModel(
    private val repository: RestaurantRepository = RestaurantRepository()
) : ViewModel() {

    var restaurantes by mutableStateOf<UiState<List<RestaurantEnLista>>>(UiState.Cargando)
        private set

    var detalle by mutableStateOf<UiState<Detalle>>(UiState.Cargando)
        private set

    var mias by mutableStateOf<List<MyReviewItem>>(emptyList())
        private set

    init { cargarRestaurantes() }

    fun cargarRestaurantes() {
        viewModelScope.launch {
            restaurantes = UiState.Cargando
            restaurantes = pedir { repository.getAllForList() }
        }
    }

    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            detalle = UiState.Cargando
            detalle = pedir { Detalle(repository.getById(id), repository.getReviews(id)) }
        }
    }

    fun cargarMisResenas() {
        viewModelScope.launch {
            val nombres = (restaurantes as? UiState.Exito)?.datos
                ?.associate { it.restaurant.id to it.restaurant.name }
                ?: emptyMap()

            mias = repository.getMyReviews().map { review ->
                MyReviewItem(
                    restaurantName = nombres[review.restaurantId] ?: "Restaurante",
                    review = review
                )
            }
        }
    }


    fun borrarResena(id: Int, alTerminar: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val borrada = repository.deleteReview(id)
                if (borrada) {
                    mias = mias.filterNot { it.review.id == id }
                    alTerminar()
                }
            } catch (e: IOException) {

            } catch (e: HttpException) {

            }
        }
    }


    fun editarResena(id: Int, stars: Int, comment: String) {
        viewModelScope.launch {
            try {
                repository.editReview(id, stars, comment)
                cargarMisResenas()
            } catch (e: IOException) {
                // por ahora lo dejamos así
            } catch (e: HttpException) {
                // igual
            }
        }
    }

    private suspend fun <T> pedir(block: suspend () -> T): UiState<T> = try {
        UiState.Exito(block())
    } catch (e: IOException) {
        UiState.Error("No hay conexión. Revisa tu internet.")
    } catch (e: HttpException) {
        UiState.Error(mensajeDe(e))
    }
}