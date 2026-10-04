package com.ganjianping.lab.ak.features.kotlin.errors

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.common.codesample.SampleLog

/** Samples for the Error handling topic. Each function body below is the code shown in its `CodeSample`. */
object ErrorHandlingSamples {
    val all: List<CodeSample> = listOf(
        CodeSample(
            title = "try/catch as an expression",
            explanation = "try returns the value of the block that ran, so a failure can turn into a fallback value in one expression.",
            code = """
                fun parse(text: String): Int = try {
                    text.toInt()
                } catch (e: NumberFormatException) {
                    -1
                }
                log("parse(12) = ${'$'}{parse("12")}")
                log("parse(twelve) = ${'$'}{parse("twelve")}")
            """.trimIndent(),
            run = { tryExpression(it) }
        ),
        CodeSample(
            title = "Custom exceptions",
            explanation = "Subclass Exception to carry details about what went wrong; catch the specific type you can handle.",
            code = """
                class InsufficientFunds(val needed: Int) : Exception("need ${'$'}needed more")
                fun withdraw(balance: Int, amount: Int): Int {
                    if (amount > balance) throw InsufficientFunds(amount - balance)
                    return balance - amount
                }
                for (amount in listOf(30, 80)) {
                    try {
                        log("left: ${'$'}{withdraw(50, amount)}")
                    } catch (e: InsufficientFunds) {
                        log("failed: ${'$'}{e.message}")
                    }
                }
            """.trimIndent(),
            run = { customExceptions(it) }
        ),
        CodeSample(
            title = "runCatching and Result",
            explanation = "runCatching turns an exception into a Result value you can pass around and handle later.",
            code = """
                fun divide(a: Int, b: Int): Result<Int> = runCatching { a / b }
                listOf(10 to 2, 1 to 0).forEach { (a, b) ->
                    divide(a, b)
                        .onSuccess { log("${'$'}a / ${'$'}b = ${'$'}it") }
                        .onFailure { log("${'$'}a / ${'$'}b failed: ${'$'}{it::class.simpleName}") }
                }
                log("getOrDefault: ${'$'}{divide(1, 0).getOrDefault(0)}")
            """.trimIndent(),
            run = { results(it) }
        ),
        CodeSample(
            title = "Preconditions with require",
            explanation = "require rejects a bad argument with IllegalArgumentException (check does the same for bad state, with IllegalStateException).",
            code = """
                fun setVolume(level: Int): String {
                    require(level in 0..10) { "level must be 0-10, was ${'$'}level" }
                    return "volume ${'$'}level"
                }
                for (level in listOf(7, 11)) {
                    val result = runCatching { setVolume(level) }
                    log(result.getOrElse { "${'$'}{it::class.simpleName}: ${'$'}{it.message}" })
                }
            """.trimIndent(),
            run = { requireAndCheck(it) }
        ),
        CodeSample(
            title = "use and finally",
            explanation = "use closes a resource whether the block succeeds or throws. It closes before the exception reaches catch.",
            code = """
                class Resource(private val name: String, private val log: SampleLog) : AutoCloseable {
                    init { log("open \"${'$'}name\"") }
                    override fun close() = log("close \"${'$'}name\"")
                }
                fun process(name: String): String = try {
                    Resource(name, log).use {
                        if (name.isEmpty()) throw IllegalArgumentException("empty")
                        log("processed ${'$'}{name.lowercase()}")
                    }
                    "ok"
                } catch (e: IllegalArgumentException) {
                    log("failed: ${'$'}{e.message}")
                    "error"
                }
                log("result: ${'$'}{process("Ada")}")
                log("result: ${'$'}{process("")}")
            """.trimIndent(),
            run = { useAndFinally(it) }
        )
    )
}

private fun tryExpression(log: SampleLog) {
    fun parse(text: String): Int = try {
        text.toInt()
    } catch (e: NumberFormatException) {
        -1
    }
    log("parse(12) = ${parse("12")}")
    log("parse(twelve) = ${parse("twelve")}")
}

private fun customExceptions(log: SampleLog) {
    class InsufficientFunds(val needed: Int) : Exception("need $needed more")
    fun withdraw(balance: Int, amount: Int): Int {
        if (amount > balance) throw InsufficientFunds(amount - balance)
        return balance - amount
    }
    for (amount in listOf(30, 80)) {
        try {
            log("left: ${withdraw(50, amount)}")
        } catch (e: InsufficientFunds) {
            log("failed: ${e.message}")
        }
    }
}

private fun results(log: SampleLog) {
    fun divide(a: Int, b: Int): Result<Int> = runCatching { a / b }
    listOf(10 to 2, 1 to 0).forEach { (a, b) ->
        divide(a, b)
            .onSuccess { log("$a / $b = $it") }
            .onFailure { log("$a / $b failed: ${it::class.simpleName}") }
    }
    log("getOrDefault: ${divide(1, 0).getOrDefault(0)}")
}

private fun requireAndCheck(log: SampleLog) {
    fun setVolume(level: Int): String {
        require(level in 0..10) { "level must be 0-10, was $level" }
        return "volume $level"
    }
    for (level in listOf(7, 11)) {
        val result = runCatching { setVolume(level) }
        log(result.getOrElse { "${it::class.simpleName}: ${it.message}" })
    }
}

private fun useAndFinally(log: SampleLog) {
    class Resource(private val name: String, private val log: SampleLog) : AutoCloseable {
        init { log("open \"$name\"") }
        override fun close() = log("close \"$name\"")
    }
    fun process(name: String): String = try {
        Resource(name, log).use {
            if (name.isEmpty()) throw IllegalArgumentException("empty")
            log("processed ${name.lowercase()}")
        }
        "ok"
    } catch (e: IllegalArgumentException) {
        log("failed: ${e.message}")
        "error"
    }
    log("result: ${process("Ada")}")
    log("result: ${process("")}")
}
