package mx.tec.sabores.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.tec.sabores.ui.components.ErrorView
import mx.tec.sabores.ui.components.RestaurantCard
import mx.tec.sabores.ui.state.SaboresViewModel
import mx.tec.sabores.ui.state.UiState

@Composable
fun RestaurantListScreen(
    viewModel: SaboresViewModel,
    onRestaurantClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (val estado = viewModel.restaurantes) {
        is UiState.Cargando -> Box(modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }
        is UiState.Error -> ErrorView(
            mensaje = estado.mensaje,
            onReintentar = { viewModel.cargarRestaurantes() }
        )
        is UiState.Exito -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(estado.datos, key = { it.restaurant.id }) { item ->
                RestaurantCard(
                    restaurant = item.restaurant,
                    summary = item.summary,
                    onClick = { onRestaurantClick(item.restaurant.id) }
                )
            }
        }
    }
}
