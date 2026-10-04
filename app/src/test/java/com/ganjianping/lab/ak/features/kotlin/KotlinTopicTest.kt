package com.ganjianping.lab.ak.features.kotlin

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.features.kotlin.basics.BasicsSamples
import com.ganjianping.lab.ak.features.kotlin.classes.ClassesSamples
import com.ganjianping.lab.ak.features.kotlin.collections.CollectionsSamples
import com.ganjianping.lab.ak.features.kotlin.coroutines.CoroutinesSamples
import com.ganjianping.lab.ak.features.kotlin.errors.ErrorHandlingSamples
import com.ganjianping.lab.ak.features.kotlin.extensions.ExtensionsSamples
import com.ganjianping.lab.ak.features.kotlin.functions.FunctionsSamples
import com.ganjianping.lab.ak.features.kotlin.generics.GenericsSamples
import com.ganjianping.lab.ak.features.kotlin.nullsafety.NullSafetySamples
import com.ganjianping.lab.ak.features.kotlin.strings.StringsRegexSamples
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Runs the Kotlin category's samples and checks what they log. The generic tests run every sample and
 * check that its snippet matches the code that runs; the topic tests check the lines that show each
 * language feature.
 */
class KotlinTopicTest {

    private class Topic(val name: String, val samples: List<CodeSample>, val source: String)

    private val topics = listOf(
        Topic("Values & types", BasicsSamples.all, "basics/BasicsSamples.kt"),
        Topic("Null safety", NullSafetySamples.all, "nullsafety/NullSafetySamples.kt"),
        Topic("Collections", CollectionsSamples.all, "collections/CollectionsSamples.kt"),
        Topic("Functions & lambdas", FunctionsSamples.all, "functions/FunctionsSamples.kt"),
        Topic("Classes, data & sealed", ClassesSamples.all, "classes/ClassesSamples.kt"),
        Topic("Interfaces & generics", GenericsSamples.all, "generics/GenericsSamples.kt"),
        Topic("Error handling", ErrorHandlingSamples.all, "errors/ErrorHandlingSamples.kt"),
        Topic("Coroutines", CoroutinesSamples.all, "coroutines/CoroutinesSamples.kt"),
        Topic("Extensions & scope functions", ExtensionsSamples.all, "extensions/ExtensionsSamples.kt"),
        Topic("Strings & regex", StringsRegexSamples.all, "strings/StringsRegexSamples.kt")
    )

    private fun output(samples: List<CodeSample>, title: String): List<String> = runBlocking {
        val sample = samples.firstOrNull { it.title == title } ?: error("No sample titled $title")
        sample.output()
    }

    // Every sample

    @Test
    fun everyTopicHasUniquelyTitledSamples() {
        topics.forEach { topic ->
            assertFalse("${topic.name} has no samples", topic.samples.isEmpty())
            assertEquals("${topic.name} repeats a title", topic.samples.size, topic.samples.map { it.title }.toSet().size)
        }
    }

    @Test
    fun everySampleLogsOutputAndShowsItsCode() = runBlocking {
        topics.forEach { topic ->
            topic.samples.forEach { sample ->
                assertTrue("${topic.name} – ${sample.title} shows no code", sample.code.isNotBlank())
                assertTrue("${topic.name} – ${sample.title} logged nothing", sample.output().isNotEmpty())
            }
        }
    }

    @Test
    fun everySampleLogsTheSameOutputEachRun() = runBlocking {
        topics.forEach { topic ->
            topic.samples.forEach { sample ->
                assertEquals("${topic.name} – ${sample.title} changed between runs", sample.output(), sample.output())
            }
        }
    }

    /**
     * The code shown is the code that runs: each sample's snippet must end with the body of its function
     * in the topic's source file. Functions appear in the same order as the samples.
     */
    @Test
    fun everySnippetMatchesTheFunctionThatRuns() {
        topics.forEach { topic ->
            val bodies = sampleFunctionBodies(File(SourceRoot, topic.source).readText())
            assertEquals("${topic.name}: one function per sample", topic.samples.size, bodies.size)
            topic.samples.zip(bodies).forEach { (sample, body) ->
                assertTrue(
                    "${topic.name} – ${sample.title}: the snippet does not match its function body",
                    sample.code.trimEnd().endsWith(body)
                )
            }
        }
    }

    // Topics

    @Test
    fun basicsShowsInferredTypesAndOverflow() {
        assertEquals(listOf("42: Int", "3.5: Double", "42.0: Double"), output(BasicsSamples.all, "Type inference"))
        val overflow = output(BasicsSamples.all, "Overflow")
        assertTrue(overflow.contains("Int.MAX_VALUE + 1 = -2147483648"))
        assertTrue(overflow.contains("Math.addExact: ArithmeticException"))
    }

    @Test
    fun nullSafetyHandlesBothValueAndNull() {
        assertEquals(
            listOf("Next year you will be 37", "\"abc\" is not a number"),
            output(NullSafetySamples.all, "Elvis and early return")
        )
        assertEquals(listOf("Hello, Ada (3 letters)!", "Hello, guest!"), output(NullSafetySamples.all, "Smart casts"))
    }

    @Test
    fun collectionsCopyAndSortResults() {
        val lists = output(CollectionsSamples.all, "Lists")
        assertEquals("sorted: [C, Kotlin, Rust]", lists.first())
        assertEquals("original: [Rust, Kotlin, C]", lists.last())
        assertEquals("both: [C]", output(CollectionsSamples.all, "Sets").first())
        assertEquals("numbers squared: 8 of 1,000,000", output(CollectionsSamples.all, "Sequences").last())
    }

    @Test
    fun lambdasKeepTheirOwnCapturedState() {
        assertEquals(listOf("first: 1, 2, 3", "second: 1, 2"), output(FunctionsSamples.all, "Capturing values"))
    }

    @Test
    fun dataCopiesAreIndependentAndClassesAreShared() {
        assertEquals(
            listOf("data copy: original 0, copy 99", "class: original 99, alias 99", "same instance: true"),
            output(ClassesSamples.all, "Copies and shared references")
        )
        assertEquals(listOf("card ending 1234", "cash 20", "voucher"), output(ClassesSamples.all, "Sealed types"))
    }

    @Test
    fun genericsRespectVariance() {
        assertEquals(
            listOf("a crate of apple", "apples sorted with a Fruit comparator: 2"),
            output(GenericsSamples.all, "Variance: out and in")
        )
        assertEquals(listOf("strings: [two, four]", "ints: [1, 5]"), output(GenericsSamples.all, "Reified type parameters"))
    }

    @Test
    fun useClosesBeforeTheExceptionIsCaught() {
        assertEquals(
            listOf(
                "open \"Ada\"", "processed ada", "close \"Ada\"", "result: ok",
                "open \"\"", "close \"\"", "failed: empty", "result: error"
            ),
            output(ErrorHandlingSamples.all, "use and finally")
        )
    }

    @Test
    fun coroutineResultsDoNotDependOnTiming() {
        assertEquals(
            listOf("flow: 4", "job: 3", "kotlin: 6", "scope: 5", "coroutineScope waited for all 4 children"),
            output(CoroutinesSamples.all, "Structured concurrency")
        )
        assertTrue(output(CoroutinesSamples.all, "async and await").contains("total: 140"))
        assertEquals(listOf("stopped early: true"), output(CoroutinesSamples.all, "Cancellation"))
        assertEquals(
            listOf("on a Default worker thread: true", "sum of squares to 1000: 333833500"),
            output(CoroutinesSamples.all, "Dispatchers and withContext")
        )
        assertEquals(listOf("tickets sold: 1000"), output(CoroutinesSamples.all, "Mutex"))
    }

    @Test
    fun extensionsAndScopeFunctions() {
        assertEquals(listOf("AL", "GBH"), output(ExtensionsSamples.all, "Extension functions"))
        assertEquals(listOf("2 × coffee"), output(ExtensionsSamples.all, "apply and run"))
        assertEquals(listOf("total: 16.25"), output(ExtensionsSamples.all, "Operator overloading"))
    }

    @Test
    fun stringsCountUtf16UnitsNotCharacters() {
        val counts = output(StringsRegexSamples.all, "Characters and Unicode")
        assertEquals(3, counts.size)
        assertTrue(counts[0].endsWith("length 4, 4 code points, 5 UTF-8 bytes"))
        assertTrue(counts[1].endsWith("length 2, 2 code points, 3 UTF-8 bytes"))
        assertTrue(counts[2].endsWith("length 4, 2 code points, 8 UTF-8 bytes"))
        assertEquals("year: 2026", output(StringsRegexSamples.all, "Regex and named groups").first())
        assertEquals(listOf("== : false", "after NFC: true"), output(StringsRegexSamples.all, "Comparing strings").take(2))
    }

    private companion object {
        // Unit tests run with the module directory as the working directory.
        val SourceRoot = File("src/main/java/com/ganjianping/lab/ak/features/kotlin")

        val SampleFunction = Regex("^private (suspend )?fun \\w+\\(log: SampleLog\\) \\{$")

        /** The dedented bodies of the `private fun name(log: SampleLog)` functions, in file order. */
        fun sampleFunctionBodies(source: String): List<String> {
            val lines = source.lines()
            return lines.indices.filter { SampleFunction.matches(lines[it]) }.map { start ->
                val end = (start + 1 until lines.size).first { lines[it] == "}" }
                lines.subList(start + 1, end).joinToString("\n") { it.removePrefix("    ") }.trimEnd()
            }
        }
    }
}
