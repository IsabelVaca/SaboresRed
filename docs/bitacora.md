# Bitácora — Práctica 4: Sabores en Red

## Ejercicio 0 — Cada endpoint, ¿a qué pantalla sirve?

| # | Endpoint | ¿Qué pantalla lo usa? |
|---|----------|------------------------|
| 1 | `GET /restaurants` | Lista, Detalle |
| 2 | `GET /restaurants/3` | Detalle |
| 3 | `GET /reviews?restaurantId=3` | Lista, Detalle |
| 4 | `POST /reviews` | Nueva reseña |
| 5 | `GET /me/reviews` | Mis reseñas |
| 6 | `DELETE /reviews/12` | Todavía no (se usa hasta el Bloque D) |

## Ejercicio A2 — ¿Quién calcula el promedio ahora?

Me quedé con los dos, cada uno para un lugar distinto:

- **`ratingAverage`**  lo uso en la lista de restaurantes, viene calculado por el servidor. 
- **`RatingSummary.from(reviews)`** lo uso en el detalle, porque ahí ya tengo las reseñas cargadas y el promedio se actualiza al instante en cuanto publico una reseña nueva

## Ejercicio B1 — ¿Cuántos estados tiene de verdad una pantalla con red?

Una pantalla con red puede estar en tres estados:

1. **Cargando** — mientras se esta pidiendo la información al servidor.
2. **Éxito** — cuando ya cargó la lista y se muestra en pantalla.
3. **Error** — cuando hubo error de red.

## Ejercicio C2 — Salta tu propia validación

Comenté la llamada a `ReviewValidator` en el ViewModel (`canSave`) y publiqué una reseña con menos de 15 caracteres.

- **Código que respondió el servidor:** `422`
- **Qué pasó:** al quitar la validación del cliente, el botón de publicar ya no se desactivaba con un comentario corto, así que sí se llegó a mandar la petición al servidor. El servidor sí valida por su cuenta: respondió con el error de que el comentario necesita un mínimo de caracteres.
- **Mensaje que mostró la app:** el mensaje de error del servidor sobre el número mínimo de caracteres
- **¿La app quedó utilizable?** Sí, no se cayó, se quedó en la pantalla de nueva reseña mostrando el error


