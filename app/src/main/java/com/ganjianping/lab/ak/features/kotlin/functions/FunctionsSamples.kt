package com.ganjianping.lab.ak.features.kotlin.functions

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.common.codesample.SampleLog

/** Samples for the Functions & lambdas topic. Each function body below is the code shown in its `CodeSample`. */
object FunctionsSamples {
    val all: List<CodeSample> = listOf(
        CodeSample(
            title = "Named and default arguments",
            explanation = "Defaults make parameters optional; naming arguments makes calls readable and lets you pass them in any order.",
            code = """
                fun greet(name: String, greeting: String = "Hello", punctuation: Char = '!') =
                    "${'$'}greeting, ${'$'}name${'$'}punctuation"
                log(greet("Ada"))
                log(greet("Ada", greeting = "Welcome"))
                log(greet(punctuation = '?', name = "Lin"))
            """.trimIndent(),
            run = { namedAndDefault(it) }
        ),
        CodeSample(
            title = "vararg",
            explanation = "vararg accepts any number of arguments as an array; * spreads an existing array into them.",
            code = """
                fun average(vararg numbers: Int): Double = if (numbers.isEmpty()) 0.0 else numbers.average()
                log("average(2, 4, 9) = ${'$'}{average(2, 4, 9)}")
                log("average() = ${'$'}{average()}")
                val scores = intArrayOf(70, 90)
                log("average(*scores) = ${'$'}{average(*scores)}")
            """.trimIndent(),
            run = { varargs(it) }
        ),
        CodeSample(
            title = "Lambdas and trailing lambdas",
            explanation = "A lambda is a function value. When it is the last argument it goes outside the parentheses, and a single parameter is called it.",
            code = """
                val square: (Int) -> Int = { it * it }
                log("square(7) = ${'$'}{square(7)}")
                val words = listOf("lambda", "fun", "it")
                log(words.sortedBy { it.length }.joinToString())
                log(words.joinToString(separator = " | ") { it.uppercase() })
            """.trimIndent(),
            run = { lambdas(it) }
        ),
        CodeSample(
            title = "Capturing values",
            explanation = "A lambda keeps the variables it uses alive. Each counter captures its own count.",
            code = """
                fun makeCounter(): () -> Int {
                    var count = 0
                    return { count += 1; count }
                }
                val first = makeCounter()
                val second = makeCounter()
                log("first: ${'$'}{first()}, ${'$'}{first()}, ${'$'}{first()}")
                log("second: ${'$'}{second()}, ${'$'}{second()}")
            """.trimIndent(),
            run = { capturing(it) }
        ),
        CodeSample(
            title = "Function references and higher-order functions",
            explanation = "::name passes an existing function as a value; a higher-order function takes or returns a function.",
            code = """
                fun isEven(number: Int) = number % 2 == 0
                fun applyTwice(value: Int, transform: (Int) -> Int) = transform(transform(value))
                log("evens: ${'$'}{(1..10).filter(::isEven)}")
                log("applyTwice(3) { it * 10 } = ${'$'}{applyTwice(3) { it * 10 }}")
            """.trimIndent(),
            run = { references(it) }
        )
    )
}

private fun namedAndDefault(log: SampleLog) {
    fun greet(name: String, greeting: String = "Hello", punctuation: Char = '!') =
        "$greeting, $name$punctuation"
    log(greet("Ada"))
    log(greet("Ada", greeting = "Welcome"))
    log(greet(punctuation = '?', name = "Lin"))
}

private fun varargs(log: SampleLog) {
    fun average(vararg numbers: Int): Double = if (numbers.isEmpty()) 0.0 else numbers.average()
    log("average(2, 4, 9) = ${average(2, 4, 9)}")
    log("average() = ${average()}")
    val scores = intArrayOf(70, 90)
    log("average(*scores) = ${average(*scores)}")
}

private fun lambdas(log: SampleLog) {
    val square: (Int) -> Int = { it * it }
    log("square(7) = ${square(7)}")
    val words = listOf("lambda", "fun", "it")
    log(words.sortedBy { it.length }.joinToString())
    log(words.joinToString(separator = " | ") { it.uppercase() })
}

private fun capturing(log: SampleLog) {
    fun makeCounter(): () -> Int {
        var count = 0
        return { count += 1; count }
    }
    val first = makeCounter()
    val second = makeCounter()
    log("first: ${first()}, ${first()}, ${first()}")
    log("second: ${second()}, ${second()}")
}

private fun references(log: SampleLog) {
    fun isEven(number: Int) = number % 2 == 0
    fun applyTwice(value: Int, transform: (Int) -> Int) = transform(transform(value))
    log("evens: ${(1..10).filter(::isEven)}")
    log("applyTwice(3) { it * 10 } = ${applyTwice(3) { it * 10 }}")
}
