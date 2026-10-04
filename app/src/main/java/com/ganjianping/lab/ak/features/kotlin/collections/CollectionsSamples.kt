package com.ganjianping.lab.ak.features.kotlin.collections

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.common.codesample.SampleLog

/** Samples for the Collections topic. Each function body below is the code shown in its `CodeSample`. */
object CollectionsSamples {
    val all: List<CodeSample> = listOf(
        CodeSample(
            title = "Lists",
            explanation = "listOf is read-only. toMutableList makes an independent copy you can change; the original stays as it was.",
            code = """
                val languages = listOf("Rust", "Kotlin", "C")
                log("sorted: ${'$'}{languages.sorted()}")
                val editable = languages.toMutableList()
                editable += "Swift"
                log("editable: ${'$'}editable")
                log("original: ${'$'}languages")
            """.trimIndent(),
            run = { lists(it) }
        ),
        CodeSample(
            title = "Sets",
            explanation = "A set holds each value once and has no order, so results are sorted before they are logged.",
            code = """
                val mobile = setOf("Kotlin", "Swift", "C")
                val systems = setOf("C", "Rust", "Zig")
                log("both: ${'$'}{(mobile intersect systems).sorted()}")
                log("either: ${'$'}{(mobile union systems).sorted()}")
                log("only mobile: ${'$'}{(mobile - systems).sorted()}")
            """.trimIndent(),
            run = { sets(it) }
        ),
        CodeSample(
            title = "Maps",
            explanation = "A map looks values up by key. Destructuring each entry gives its key and value.",
            code = """
                val stock = mutableMapOf("apple" to 3, "pear" to 0)
                stock["kiwi"] = 5
                stock["apple"] = stock.getValue("apple") - 1
                for ((fruit, count) in stock.toSortedMap()) {
                    log("${'$'}fruit: ${'$'}count")
                }
                log("plum: ${'$'}{stock.getOrDefault("plum", 0)}")
            """.trimIndent(),
            run = { maps(it) }
        ),
        CodeSample(
            title = "map, filter, and fold",
            explanation = "Higher-order functions transform a collection without a loop; each returns a new list or value.",
            code = """
                val prices = listOf(12, 5, 30, 8)
                val discounted = prices.map { it * 9 / 10 }
                val cheap = prices.filter { it < 10 }
                val total = prices.fold(0) { sum, price -> sum + price }
                log("discounted: ${'$'}discounted")
                log("cheap: ${'$'}cheap")
                log("total: ${'$'}total")
            """.trimIndent(),
            run = { higherOrder(it) }
        ),
        CodeSample(
            title = "Sequences",
            explanation = "A sequence is lazy: it computes items one at a time and stops as soon as the answer is known.",
            code = """
                val evaluated = mutableListOf<Int>()
                val firstBigSquare = (1..1_000_000).asSequence()
                    .map { evaluated += it; it * it }
                    .first { it > 50 }
                log("first square over 50: ${'$'}firstBigSquare")
                log("numbers squared: ${'$'}{evaluated.size} of 1,000,000")
            """.trimIndent(),
            run = { sequences(it) }
        )
    )
}

private fun lists(log: SampleLog) {
    val languages = listOf("Rust", "Kotlin", "C")
    log("sorted: ${languages.sorted()}")
    val editable = languages.toMutableList()
    editable += "Swift"
    log("editable: $editable")
    log("original: $languages")
}

private fun sets(log: SampleLog) {
    val mobile = setOf("Kotlin", "Swift", "C")
    val systems = setOf("C", "Rust", "Zig")
    log("both: ${(mobile intersect systems).sorted()}")
    log("either: ${(mobile union systems).sorted()}")
    log("only mobile: ${(mobile - systems).sorted()}")
}

private fun maps(log: SampleLog) {
    val stock = mutableMapOf("apple" to 3, "pear" to 0)
    stock["kiwi"] = 5
    stock["apple"] = stock.getValue("apple") - 1
    for ((fruit, count) in stock.toSortedMap()) {
        log("$fruit: $count")
    }
    log("plum: ${stock.getOrDefault("plum", 0)}")
}

private fun higherOrder(log: SampleLog) {
    val prices = listOf(12, 5, 30, 8)
    val discounted = prices.map { it * 9 / 10 }
    val cheap = prices.filter { it < 10 }
    val total = prices.fold(0) { sum, price -> sum + price }
    log("discounted: $discounted")
    log("cheap: $cheap")
    log("total: $total")
}

private fun sequences(log: SampleLog) {
    val evaluated = mutableListOf<Int>()
    val firstBigSquare = (1..1_000_000).asSequence()
        .map { evaluated += it; it * it }
        .first { it > 50 }
    log("first square over 50: $firstBigSquare")
    log("numbers squared: ${evaluated.size} of 1,000,000")
}
