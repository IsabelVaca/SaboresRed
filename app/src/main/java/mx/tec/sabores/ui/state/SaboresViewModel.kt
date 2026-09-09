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
import mx.tec.sabores.domain.Review
import mx.tec.sabores.domain.ReviewValidator
import mx.tec.sabores.domain.RestaurantEnLista

data class MyReviewItem(val restaurantName: String, val review: Review)

class SaboresViewModel : ViewModel() {

    private val repository = RestaurantRepository()

    var restaurantesConResumen by mutableStateOf<List<RestaurantEnLista>>(emptyList())
        private set

    val restaurants: List<Restaurant>
        get() = restaurantesConResumen.map { it.restaurant }

    var reviews by mutableStateOf<List<Review>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            restaurantesConResumen = repository.getAllForList()
        }
    }


    val myReviews: List<MyReviewItem>
        get() = reviews.reversed().mapNotNull { review ->
            restaurantById(review.restaurantId)?.let { MyReviewItem(it.name, review) }
        }

    fun restaurantById(id: Int): Restaurant? =
        restaurants.firstOrNull { it.id == id }

    fun reviewsOf(restaurantId: Int): List<Review> =
        reviews.filter { it.restaurantId == restaurantId }

    fun summaryOf(restaurantId: Int): RatingSummary =
        restaurantesConResumen.firstOrNull { it.restaurant.id == restaurantId }?.summary
            ?: RatingSummary(0.0, 0)

    fun addReview(restaurantId: Int, stars: Int, comment: String) {
        if (!ReviewValidator.isValid(stars, comment)) return
        val newId = (reviews.maxOfOrNull { it.id } ?: 0) + 1
        reviews = reviews + Review(
            id = newId,
            restaurantId = restaurantId,
            author = "Yo",
            stars = stars,
            comment = comment.trim()
        )
    }
}