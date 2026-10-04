package com.ganjianping.lab.ak.features.kotlin.basics

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.common.codesample.SampleLog

/** Samples for the Values & types topic. Each function body below is the code shown in its `CodeSample`. */
object BasicsSamples {
    val all: List<CodeSample> = listOf(
        CodeSample(
            title = "val and var",
            explanation = "val is read-only once assigned; var can change. Prefer val, and use var only when a value must change.",
            code = """
                val language = "Kotlin"   // read-only
                var version = 2.0          // can change
                log("${'$'}language ${'$'}version")
                version = 2.4
                log("${'$'}language ${'$'}version")
                // language = "Java"  would not compile: val cannot be reassigned
            """.trimIndent(),
            run = { valAndVar(it) }
        ),
        CodeSample(
            title = "Type inference",
            explanation = "Kotlin infers a type from the value. It never widens numbers silently, so Int to Double needs toDouble().",
            code = """
                val whole = 42
                val fraction = 3.5
                val converted = whole.toDouble()   // `val d: Double = whole` would not compile
                log("${'$'}whole: ${'$'}{whole::class.simpleName}")
                log("${'$'}fraction: ${'$'}{fraction::class.simpleName}")
                log("${'$'}converted: ${'$'}{converted::class.simpleName}")
            """.trimIndent(),
            run = { typeInference(it) }
        ),
        CodeSample(
            title = "Overflow",
            explanation = "Int arithmetic wraps around silently. Math.addExact throws instead, and Long has room for bigger values.",
            code = """
                val max = Int.MAX_VALUE
                val wrapped = max + 1
                log("Int.MAX_VALUE + 1 = ${'$'}wrapped")
                val checked = runCatching { Math.addExact(max, 1) }
                log("Math.addExact: ${'$'}{checked.exceptionOrNull()?.javaClass?.simpleName}")
                log("as Long: ${'$'}{max.toLong() + 1}")
            """.trimIndent(),
            run = { overflow(it) }
        ),
        CodeSample(
            title = "String templates",
            explanation = "${'$'}name inserts a value into a string; ${'$'}{…} inserts any expression.",
            code = """
                val name = "Ada"
                val score = 65
                log("${'$'}name scored ${'$'}score of 100")
                log("passed: ${'$'}{score >= 50}")
                log("in capitals: ${'$'}{name.uppercase()}")
            """.trimIndent(),
            run = { templates(it) }
        ),
        CodeSample(
            title = "Pairs, destructuring, and when",
            explanation = "to builds a Pair, destructuring unpacks it, and when is an expression that returns the first matching branch.",
            code = """
                val (city, temperature) = "Singapore" to 31
                val feeling = when {
                    temperature >= 30 -> "hot"
                    temperature >= 20 -> "warm"
                    else -> "cool"
                }
                log("${'$'}city is ${'$'}feeling at ${'$'}temperature°C")
            """.trimIndent(),
            run = { pairsAndWhen(it) }
        )
    )
}

private fun valAndVar(log: SampleLog) {
    val language = "Kotlin"   // read-only
    var version = 2.0          // can change
    log("$language $version")
    version = 2.4
    log("$language $version")
    // language = "Java"  would not compile: val cannot be reassigned
}

private fun typeInference(log: SampleLog) {
    val whole = 42
    val fraction = 3.5
    val converted = whole.toDouble()   // `val d: Double = whole` would not compile
    log("$whole: ${whole::class.simpleName}")
    log("$fraction: ${fraction::class.simpleName}")
    log("$converted: ${converted::class.simpleName}")
}

private fun overflow(log: SampleLog) {
    val max = Int.MAX_VALUE
    val wrapped = max + 1
    log("Int.MAX_VALUE + 1 = $wrapped")
    val checked = runCatching { Math.addExact(max, 1) }
    log("Math.addExact: ${checked.exceptionOrNull()?.javaClass?.simpleName}")
    log("as Long: ${max.toLong() + 1}")
}

private fun templates(log: SampleLog) {
    val name = "Ada"
    val score = 65
    log("$name scored $score of 100")
    log("passed: ${score >= 50}")
    log("in capitals: ${name.uppercase()}")
}

private fun pairsAndWhen(log: SampleLog) {
    val (city, temperature) = "Singapore" to 31
    val feeling = when {
        temperature >= 30 -> "hot"
        temperature >= 20 -> "warm"
        else -> "cool"
    }
    log("$city is $feeling at $temperature°C")
}
