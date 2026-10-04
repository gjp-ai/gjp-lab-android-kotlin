package com.ganjianping.lab.ak.features.compose.lists

import java.text.Normalizer

enum class ProduceKind(val title: String) { Fruit("Fruit"), Vegetable("Vegetables") }

/** One sample item for the Lists & grids topic. */
data class Produce(
    val id: Int,
    val name: String,
    val emoji: String,
    val kind: ProduceKind,
    val isFavorite: Boolean = false
) {
    companion object {
        val samples: List<Produce> = listOf(
            Produce(1, "Apple", "🍎", ProduceKind.Fruit),
            Produce(2, "Banana", "🍌", ProduceKind.Fruit),
            Produce(3, "Cherry", "🍒", ProduceKind.Fruit),
            Produce(4, "Grapes", "🍇", ProduceKind.Fruit),
            Produce(5, "Kiwi", "🥝", ProduceKind.Fruit),
            Produce(6, "Lemon", "🍋", ProduceKind.Fruit),
            Produce(7, "Mango", "🥭", ProduceKind.Fruit),
            Produce(8, "Orange", "🍊", ProduceKind.Fruit),
            Produce(9, "Peach", "🍑", ProduceKind.Fruit),
            Produce(10, "Pineapple", "🍍", ProduceKind.Fruit),
            Produce(11, "Avocado", "🥑", ProduceKind.Vegetable),
            Produce(12, "Broccoli", "🥦", ProduceKind.Vegetable),
            Produce(13, "Carrot", "🥕", ProduceKind.Vegetable),
            Produce(14, "Corn", "🌽", ProduceKind.Vegetable),
            Produce(15, "Cucumber", "🥒", ProduceKind.Vegetable),
            Produce(16, "Eggplant", "🍆", ProduceKind.Vegetable),
            Produce(17, "Garlic", "🧄", ProduceKind.Vegetable),
            Produce(18, "Jalapeño", "🌶️", ProduceKind.Vegetable),
            Produce(19, "Onion", "🧅", ProduceKind.Vegetable),
            Produce(20, "Potato", "🥔", ProduceKind.Vegetable)
        )
    }
}

/**
 * Items whose name contains [query], ignoring case and accents ("jalapeno" finds "Jalapeño").
 * A blank query matches everything.
 */
fun List<Produce>.matching(query: String): List<Produce> {
    val needle = query.trim().foldForSearch()
    if (needle.isEmpty()) return this
    return filter { it.name.foldForSearch().contains(needle) }
}

private val CombiningMarks = Regex("\\p{Mn}+")

/** Lower-cases text and strips accents by decomposing characters and dropping the combining marks. */
internal fun String.foldForSearch(): String =
    CombiningMarks.replace(Normalizer.normalize(this, Normalizer.Form.NFD), "").lowercase()
