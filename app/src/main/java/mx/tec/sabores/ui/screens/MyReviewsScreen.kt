package mx.tec.sabores.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.tec.sabores.ui.components.StarsRow
import mx.tec.sabores.ui.state.MyReviewItem
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import mx.tec.sabores.ui.components.StarPicker

@Composable
fun MyReviewsScreen(
    items: List<MyReviewItem>,
    onEditar: (id: Int, stars: Int, comment: String) -> Unit,
    onBorrar: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Todavía no has reseñado ningún lugar.",
                 color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.review.id }) { item ->
            var editando by remember(item.review.id) { mutableStateOf(false) }
            var stars by remember(item.review.id) { mutableStateOf(item.review.stars) }
            var comment by remember(item.review.id) { mutableStateOf(item.review.comment) }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(item.restaurantName, style = MaterialTheme.typography.titleMedium)

                    if (editando) {
                        StarPicker(value = stars, onValueChange = { stars = it })
                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {
                                onEditar(item.review.id, stars, comment)
                                editando = false
                            }) { Text("Guardar") }
                            OutlinedButton(onClick = {
                                stars = item.review.stars
                                comment = item.review.comment
                                editando = false
                            }) { Text("Cancelar") }
                        }
                    } else {
                        StarsRow(item.review.stars)
                        Spacer(Modifier.height(6.dp))
                        Text(item.review.comment, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { editando = true }) { Text("Editar") }
                            OutlinedButton(onClick = { onBorrar(item.review.id) }) { Text("Borrar") }
                        }
                    }
                }
            }
        }
    }
}
