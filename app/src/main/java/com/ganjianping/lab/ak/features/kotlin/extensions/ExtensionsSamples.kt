package com.ganjianping.lab.ak.features.kotlin.extensions

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.common.codesample.SampleLog

/**
 * Samples for the Extensions & scope functions topic. Each function body below is the code shown in its
 * `CodeSample`. Extension properties and infix functions cannot be declared inside a function, so they
 * live at file level and the snippet shows them above the code that uses them.
 */
object ExtensionsSamples {
    val all: List<CodeSample> = listOf(
        CodeSample(
            title = "Extension functions",
            explanation = "An extension adds a function to an existing type without changing or subclassing it. Inside it, this is the receiver.",
            code = """
                fun String.initials(): String = split(" ").joinToString("") { it.first().uppercase() }
                log("Ada Lovelace".initials())
                log("grace brewster hopper".initials())
            """.trimIndent(),
            run = { extensionFunctions(it) }
        ),
        CodeSample(
            title = "Extension properties",
            explanation = "An extension property computes a value from the receiver; it cannot store state of its own.",
            code = """
                val Int.isEven: Boolean get() = this % 2 == 0

                log("4.isEven = ${'$'}{4.isEven}")
                log("7.isEven = ${'$'}{7.isEven}")
            """.trimIndent(),
            run = { extensionProperties(it) }
        ),
        CodeSample(
            title = "let and also",
            explanation = "let passes the value as it and returns the block's result; also does something with it and returns the value itself.",
            code = """
                val nickname: String? = listOf("Ada", null).first()
                val length = nickname?.let { it.length } ?: 0
                log("length: ${'$'}length")
                val numbers = mutableListOf(1, 2).also { log("before adding: ${'$'}it") }
                numbers += 3
                log("after adding: ${'$'}numbers")
            """.trimIndent(),
            run = { letAndAlso(it) }
        ),
        CodeSample(
            title = "apply and run",
            explanation = "apply configures an object with this as the receiver and returns it; run computes a result from it.",
            code = """
                class Order {
                    var item = ""
                    var quantity = 0
                }
                val order = Order().apply {
                    item = "coffee"
                    quantity = 2
                }
                val summary = order.run { "${'$'}quantity × ${'$'}item" }
                log(summary)
            """.trimIndent(),
            run = { applyAndRun(it) }
        ),
        CodeSample(
            title = "with and infix functions",
            explanation = "with runs a block on an object you already have. infix lets a one-argument function be called without dots or parentheses.",
            code = """
                infix fun Int.percentOf(total: Int): Int = this * total / 100

                log("15 percentOf 80 = ${'$'}{15 percentOf 80}")
                val report = with(StringBuilder()) {
                    append("tip: ")
                    append(15 percentOf 80)
                    toString()
                }
                log(report)
            """.trimIndent(),
            run = { withAndInfix(it) }
        ),
        CodeSample(
            title = "Operator overloading",
            explanation = "An operator function gives a symbol such as + a meaning for your type.",
            code = """
                data class Money(val cents: Int) {
                    operator fun plus(other: Money) = Money(cents + other.cents)
                }
                val total = Money(1_250) + Money(375)
                log("total: ${'$'}{total.cents / 100}.${'$'}{(total.cents % 100).toString().padStart(2, '0')}")
            """.trimIndent(),
            run = { operators(it) }
        )
    )
}

private val Int.isEven: Boolean get() = this % 2 == 0

private infix fun Int.percentOf(total: Int): Int = this * total / 100

private fun extensionFunctions(log: SampleLog) {
    fun String.initials(): String = split(" ").joinToString("") { it.first().uppercase() }
    log("Ada Lovelace".initials())
    log("grace brewster hopper".initials())
}

private fun extensionProperties(log: SampleLog) {
    log("4.isEven = ${4.isEven}")
    log("7.isEven = ${7.isEven}")
}

private fun letAndAlso(log: SampleLog) {
    val nickname: String? = listOf("Ada", null).first()
    val length = nickname?.let { it.length } ?: 0
    log("length: $length")
    val numbers = mutableListOf(1, 2).also { log("before adding: $it") }
    numbers += 3
    log("after adding: $numbers")
}

private fun applyAndRun(log: SampleLog) {
    class Order {
        var item = ""
        var quantity = 0
    }
    val order = Order().apply {
        item = "coffee"
        quantity = 2
    }
    val summary = order.run { "$quantity × $item" }
    log(summary)
}

private fun withAndInfix(log: SampleLog) {
    log("15 percentOf 80 = ${15 percentOf 80}")
    val report = with(StringBuilder()) {
        append("tip: ")
        append(15 percentOf 80)
        toString()
    }
    log(report)
}

private fun operators(log: SampleLog) {
    data class Money(val cents: Int) {
        operator fun plus(other: Money) = Money(cents + other.cents)
    }
    val total = Money(1_250) + Money(375)
    log("total: ${total.cents / 100}.${(total.cents % 100).toString().padStart(2, '0')}")
}
