package com.ganjianping.lab.ak.common.codesample

/**
 * One runnable Kotlin sample: the code shown on screen and the function that runs it. [run] is that
 * code, so the output is real, not hard-coded text. Keep [code] identical to the body of [run].
 */
class CodeSample(
    val title: String,
    val explanation: String,
    val code: String,
    val run: suspend (SampleLog) -> Unit
) {
    /** Runs the sample with a fresh log and returns the lines it logged. */
    suspend fun output(): List<String> {
        val log = SampleLog()
        run(log)
        return log.lines
    }
}

/**
 * Collects the lines a sample prints. Samples write `log("…")` where a script would write `println("…")`.
 * It is synchronized because coroutine samples may log from background threads.
 */
class SampleLog {
    private val collected = mutableListOf<String>()

    val lines: List<String>
        get() = synchronized(collected) { collected.toList() }

    operator fun invoke(line: String) {
        synchronized(collected) { collected += line }
    }
}
