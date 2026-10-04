package com.ganjianping.lab.ak.features.kotlin.generics

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.common.codesample.SampleLog
import java.util.Locale

/**
 * Samples for the Interfaces & generics topic. Each function body below is the code shown in its
 * `CodeSample`. Interfaces and inline functions cannot be declared inside a function, so they live at
 * file level and the snippet shows them above the code that uses them.
 */
object GenericsSamples {
    val all: List<CodeSample> = listOf(
        CodeSample(
            title = "Interfaces with default methods",
            explanation = "An interface declares what a type can do and may provide a default implementation that every implementer gets.",
            code = """
                interface Shape {
                    val name: String
                    fun area(): Double
                    fun describe() = "${'$'}name with area ${'$'}{"%.1f".format(Locale.ROOT, area())}"
                }
                class Square(private val side: Double) : Shape {
                    override val name = "Square"
                    override fun area() = side * side
                }
                class Disc(private val radius: Double) : Shape {
                    override val name = "Disc"
                    override fun area() = Math.PI * radius * radius
                }

                listOf(Square(2.0), Disc(1.0)).forEach { log(it.describe()) }
            """.trimIndent(),
            run = { interfaces(it) }
        ),
        CodeSample(
            title = "Generic functions and constraints",
            explanation = "A type parameter lets one function work with many types; the bound T : Comparable<T> requires values that can be compared.",
            code = """
                fun <T : Comparable<T>> largest(items: List<T>): T? = items.maxOrNull()
                log("largest(3, 9, 4) = ${'$'}{largest(listOf(3, 9, 4))}")
                log("largest(pear, apple) = ${'$'}{largest(listOf("pear", "apple"))}")
                log("largest(empty) = ${'$'}{largest(emptyList<Int>())}")
            """.trimIndent(),
            run = { genericFunctions(it) }
        ),
        CodeSample(
            title = "Generic classes",
            explanation = "A generic class is written once and checked for each element type; a Stack<String> only accepts strings.",
            code = """
                class Stack<T> {
                    private val items = mutableListOf<T>()
                    val size: Int get() = items.size
                    fun push(item: T) { items += item }
                    fun pop(): T? = items.removeLastOrNull()
                }
                val stack = Stack<String>()
                stack.push("first")
                stack.push("second")
                log("pop: ${'$'}{stack.pop()}, size: ${'$'}{stack.size}")
                // stack.push(3)  would not compile: 3 is not a String
            """.trimIndent(),
            run = { genericClasses(it) }
        ),
        CodeSample(
            title = "Variance: out and in",
            explanation = "out T means a type only produces T, so a Crate<Apple> can be used as a Crate<Fruit>. Comparator is in T: a Fruit comparator can sort apples.",
            code = """
                open class Fruit(val name: String)
                class Apple : Fruit("apple")
                class Crate<out T>(private val item: T) { fun open(): T = item }

                fun describe(crate: Crate<Fruit>) = "a crate of ${'$'}{crate.open().name}"
                val appleCrate: Crate<Apple> = Crate(Apple())
                log(describe(appleCrate))
                val byName: Comparator<Fruit> = compareBy { it.name }
                log("apples sorted with a Fruit comparator: ${'$'}{listOf(Apple(), Apple()).sortedWith(byName).size}")
            """.trimIndent(),
            run = { variance(it) }
        ),
        CodeSample(
            title = "Reified type parameters",
            explanation = "Generic types are erased at run time, but an inline function with reified T can still check against T.",
            code = """
                inline fun <reified T> List<Any>.only(): List<T> = filterIsInstance<T>()

                val mixed: List<Any> = listOf(1, "two", 3.0, "four", 5)
                log("strings: ${'$'}{mixed.only<String>()}")
                log("ints: ${'$'}{mixed.only<Int>()}")
            """.trimIndent(),
            run = { reified(it) }
        )
    )
}

private interface Shape {
    val name: String
    fun area(): Double
    fun describe() = "$name with area ${"%.1f".format(Locale.ROOT, area())}"
}

private class Square(private val side: Double) : Shape {
    override val name = "Square"
    override fun area() = side * side
}

private class Disc(private val radius: Double) : Shape {
    override val name = "Disc"
    override fun area() = Math.PI * radius * radius
}

private inline fun <reified T> List<Any>.only(): List<T> = filterIsInstance<T>()

private fun interfaces(log: SampleLog) {
    listOf(Square(2.0), Disc(1.0)).forEach { log(it.describe()) }
}

private fun genericFunctions(log: SampleLog) {
    fun <T : Comparable<T>> largest(items: List<T>): T? = items.maxOrNull()
    log("largest(3, 9, 4) = ${largest(listOf(3, 9, 4))}")
    log("largest(pear, apple) = ${largest(listOf("pear", "apple"))}")
    log("largest(empty) = ${largest(emptyList<Int>())}")
}

private fun genericClasses(log: SampleLog) {
    class Stack<T> {
        private val items = mutableListOf<T>()
        val size: Int get() = items.size
        fun push(item: T) { items += item }
        fun pop(): T? = items.removeLastOrNull()
    }
    val stack = Stack<String>()
    stack.push("first")
    stack.push("second")
    log("pop: ${stack.pop()}, size: ${stack.size}")
    // stack.push(3)  would not compile: 3 is not a String
}

private fun variance(log: SampleLog) {
    open class Fruit(val name: String)
    class Apple : Fruit("apple")
    class Crate<out T>(private val item: T) { fun open(): T = item }

    fun describe(crate: Crate<Fruit>) = "a crate of ${crate.open().name}"
    val appleCrate: Crate<Apple> = Crate(Apple())
    log(describe(appleCrate))
    val byName: Comparator<Fruit> = compareBy { it.name }
    log("apples sorted with a Fruit comparator: ${listOf(Apple(), Apple()).sortedWith(byName).size}")
}

private fun reified(log: SampleLog) {
    val mixed: List<Any> = listOf(1, "two", 3.0, "four", 5)
    log("strings: ${mixed.only<String>()}")
    log("ints: ${mixed.only<Int>()}")
}
