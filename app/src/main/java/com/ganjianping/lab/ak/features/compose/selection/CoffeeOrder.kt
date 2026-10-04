package com.ganjianping.lab.ak.features.compose.selection

enum class CoffeeSize(val title: String) { Small("Small"), Medium("Medium"), Large("Large") }

enum class Temperature(val title: String) { Hot("Hot"), Iced("Iced") }

enum class Extra(val title: String) { Vanilla("Vanilla"), Caramel("Caramel"), Cinnamon("Cinnamon"), OatMilk("Oat milk") }

/** Every choice on the Selection screen, combined into one order. Free of Compose so tests can check it. */
data class CoffeeOrder(
    val size: CoffeeSize = CoffeeSize.Medium,
    val temperature: Temperature = Temperature.Hot,
    val isDecaf: Boolean = false,
    val shots: Int = 2,
    val sweetness: Int = 50,
    val extras: Set<Extra> = setOf(Extra.Cinnamon)
) {
    /** For example "Large iced decaf coffee, 2 shots, 50% sweet, with Vanilla and Oat milk." */
    val summary: String
        get() {
            val coffee = buildList {
                // Qualified: inside buildList, a bare `size` would be the list's size.
                add(this@CoffeeOrder.size.title)
                add(temperature.title.lowercase())
                if (isDecaf) add("decaf")
                add("coffee")
            }.joinToString(" ")
            val shotText = if (shots == 1) "1 shot" else "$shots shots"
            // Extras are listed in their declared order, whatever order they were picked in.
            val chosen = Extra.entries.filter { it in extras }.map { it.title }
            val extrasText = when (chosen.size) {
                0 -> "no extras"
                1 -> "with ${chosen[0]}"
                else -> "with ${chosen.dropLast(1).joinToString(", ")} and ${chosen.last()}"
            }
            return "$coffee, $shotText, $sweetness% sweet, $extrasText."
        }

    companion object {
        val ShotRange = 1..4
        val SweetnessRange = 0..100
    }
}
