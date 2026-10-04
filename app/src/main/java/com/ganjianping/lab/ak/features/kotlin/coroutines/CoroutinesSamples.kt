package com.ganjianping.lab.ak.features.kotlin.coroutines

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.common.codesample.SampleLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

/**
 * Samples for the Coroutines topic. Each function body below is the code shown in its `CodeSample`.
 * Output never depends on which coroutine finishes first: parallel results are sorted before they are
 * logged, and timing is only compared against a generous limit.
 */
object CoroutinesSamples {
    val all: List<CodeSample> = listOf(
        CodeSample(
            title = "suspend and delay",
            explanation = "A suspend function can pause without blocking its thread. delay stands in for slow work such as a network call.",
            code = """
                suspend fun fetchGreeting(name: String): String {
                    delay(300)   // the thread is free while this waits
                    return "Hello, ${'$'}name"
                }
                log("before")
                log(fetchGreeting("Ada"))
                log("after")
            """.trimIndent(),
            run = { suspendAndDelay(it) }
        ),
        CodeSample(
            title = "async and await",
            explanation = "async starts work that returns a value; awaiting several at once runs them in parallel instead of one after another.",
            code = """
                suspend fun price(item: String): Int {
                    delay(400)
                    return item.length * 10
                }
                val elapsed = measureTimeMillis {
                    coroutineScope {
                        val apple = async { price("apple") }
                        val melon = async { price("melon") }
                        val kiwi = async { price("kiwi") }
                        log("total: ${'$'}{apple.await() + melon.await() + kiwi.await()}")
                    }
                }
                log("three 0.4 s waits overlapped: ${'$'}{elapsed < 1_200}")
            """.trimIndent(),
            run = { asyncAndAwait(it) }
        ),
        CodeSample(
            title = "Structured concurrency",
            explanation = "coroutineScope waits for every child it starts. Children finish in any order, so the results are sorted.",
            code = """
                val lengths = coroutineScope {
                    listOf("kotlin", "flow", "scope", "job").map { word ->
                        async { word to word.length }
                    }.awaitAll()
                }
                lengths.sortedBy { it.first }.forEach { (word, length) -> log("${'$'}word: ${'$'}length") }
                log("coroutineScope waited for all ${'$'}{lengths.size} children")
            """.trimIndent(),
            run = { structured(it) }
        ),
        CodeSample(
            title = "Cancellation",
            explanation = "Cancelling only asks a coroutine to stop. It stops at the next check: isActive, delay, or another suspending call.",
            code = """
                var count = 0
                coroutineScope {
                    val job = launch {
                        while (isActive && count < 1_000) {
                            count++
                            delay(10)
                        }
                    }
                    delay(100)
                    job.cancelAndJoin()
                }
                log("stopped early: ${'$'}{count < 1_000}")
            """.trimIndent(),
            run = { cancellation(it) }
        ),
        CodeSample(
            title = "Dispatchers and withContext",
            explanation = "withContext moves a block to another dispatcher and returns its result. Dispatchers.Default suits CPU-heavy work.",
            code = """
                val sum = withContext(Dispatchers.Default) {
                    val worker = Thread.currentThread().name.startsWith("DefaultDispatcher")
                    log("on a Default worker thread: ${'$'}worker")
                    (1L..1_000L).sumOf { it * it }
                }
                log("sum of squares to 1000: ${'$'}sum")
            """.trimIndent(),
            run = { dispatchers(it) }
        ),
        CodeSample(
            title = "Mutex",
            explanation = "Many coroutines changing one value at once can lose updates. A Mutex lets one coroutine in at a time.",
            code = """
                val mutex = Mutex()
                var sold = 0
                withContext(Dispatchers.Default) {
                    repeat(1_000) {
                        launch { mutex.withLock { sold++ } }
                    }
                }
                log("tickets sold: ${'$'}sold")
            """.trimIndent(),
            run = { mutexSample(it) }
        )
    )
}

private suspend fun suspendAndDelay(log: SampleLog) {
    suspend fun fetchGreeting(name: String): String {
        delay(300)   // the thread is free while this waits
        return "Hello, $name"
    }
    log("before")
    log(fetchGreeting("Ada"))
    log("after")
}

private suspend fun asyncAndAwait(log: SampleLog) {
    suspend fun price(item: String): Int {
        delay(400)
        return item.length * 10
    }
    val elapsed = measureTimeMillis {
        coroutineScope {
            val apple = async { price("apple") }
            val melon = async { price("melon") }
            val kiwi = async { price("kiwi") }
            log("total: ${apple.await() + melon.await() + kiwi.await()}")
        }
    }
    log("three 0.4 s waits overlapped: ${elapsed < 1_200}")
}

private suspend fun structured(log: SampleLog) {
    val lengths = coroutineScope {
        listOf("kotlin", "flow", "scope", "job").map { word ->
            async { word to word.length }
        }.awaitAll()
    }
    lengths.sortedBy { it.first }.forEach { (word, length) -> log("$word: $length") }
    log("coroutineScope waited for all ${lengths.size} children")
}

private suspend fun cancellation(log: SampleLog) {
    var count = 0
    coroutineScope {
        val job = launch {
            while (isActive && count < 1_000) {
                count++
                delay(10)
            }
        }
        delay(100)
        job.cancelAndJoin()
    }
    log("stopped early: ${count < 1_000}")
}

private suspend fun dispatchers(log: SampleLog) {
    val sum = withContext(Dispatchers.Default) {
        val worker = Thread.currentThread().name.startsWith("DefaultDispatcher")
        log("on a Default worker thread: $worker")
        (1L..1_000L).sumOf { it * it }
    }
    log("sum of squares to 1000: $sum")
}

private suspend fun mutexSample(log: SampleLog) {
    val mutex = Mutex()
    var sold = 0
    withContext(Dispatchers.Default) {
        repeat(1_000) {
            launch { mutex.withLock { sold++ } }
        }
    }
    log("tickets sold: $sold")
}
