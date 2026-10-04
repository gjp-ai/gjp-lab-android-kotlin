package com.ganjianping.lab.ak.features.kotlin.classes

import com.ganjianping.lab.ak.common.codesample.CodeSample
import com.ganjianping.lab.ak.common.codesample.SampleLog

/**
 * Samples for the Classes, data & sealed topic. Each function body below is the code shown in its
 * `CodeSample`. Enums and sealed types cannot be declared inside a function, so they live at file level
 * and the snippet shows them above the code that uses them.
 */
object ClassesSamples {
    val all: List<CodeSample> = listOf(
        CodeSample(
            title = "Classes and properties",
            explanation = "A class bundles state and behaviour. A private setter lets everyone read a property but only the class change it.",
            code = """
                class Account(val owner: String, balance: Int) {
                    var balance = balance
                        private set
                    fun deposit(amount: Int) {
                        require(amount > 0) { "amount must be positive" }
                        balance += amount
                    }
                }
                val account = Account("Ada", 100)
                account.deposit(50)
                log("${'$'}{account.owner}: ${'$'}{account.balance}")
            """.trimIndent(),
            run = { classesAndProperties(it) }
        ),
        CodeSample(
            title = "Data classes",
            explanation = "data generates equals, hashCode, toString, copy, and destructuring from the constructor properties.",
            code = """
                data class Point(val x: Int, val y: Int)
                val a = Point(1, 2)
                val b = Point(1, 2)
                val moved = a.copy(x = 9)
                log("a == b: ${'$'}{a == b}")
                log("a === b: ${'$'}{a === b}")
                log("moved: ${'$'}moved")
                val (x, y) = moved
                log("x = ${'$'}x, y = ${'$'}y")
            """.trimIndent(),
            run = { dataClasses(it) }
        ),
        CodeSample(
            title = "Copies and shared references",
            explanation = "Every class instance is shared by reference. copy() on a data class gives an independent value; assigning a class instance only adds another name for it.",
            code = """
                data class Settings(val volume: Int)
                class Box(var volume: Int)
                val original = Settings(0)
                val copy = original.copy(volume = 99)
                log("data copy: original ${'$'}{original.volume}, copy ${'$'}{copy.volume}")
                val box = Box(0)
                val sameBox = box
                sameBox.volume = 99
                log("class: original ${'$'}{box.volume}, alias ${'$'}{sameBox.volume}")
                log("same instance: ${'$'}{box === sameBox}")
            """.trimIndent(),
            run = { copiesAndReferences(it) }
        ),
        CodeSample(
            title = "Enums and when",
            explanation = "An enum lists every possible value. A when over an enum needs no else when it covers every entry.",
            code = """
                enum class Planet(val moons: Int) { Mercury(0), Earth(1), Mars(2) }

                for (planet in Planet.entries) {
                    val description = when (planet) {
                        Planet.Mercury -> "too hot"
                        Planet.Earth -> "home"
                        Planet.Mars -> "next stop"
                    }
                    log("${'$'}{planet.name}: ${'$'}{planet.moons} moons, ${'$'}description")
                }
            """.trimIndent(),
            run = { enums(it) }
        ),
        CodeSample(
            title = "Sealed types",
            explanation = "A sealed interface fixes its subtypes at compile time, so when can check every case, and each case can carry its own data.",
            code = """
                sealed interface Payment
                data class Card(val last4: String) : Payment
                data class Cash(val amount: Int) : Payment
                data object Voucher : Payment

                fun describe(payment: Payment) = when (payment) {   // no else needed
                    is Card -> "card ending ${'$'}{payment.last4}"
                    is Cash -> "cash ${'$'}{payment.amount}"
                    Voucher -> "voucher"
                }
                listOf(Card("1234"), Cash(20), Voucher).forEach { log(describe(it)) }
            """.trimIndent(),
            run = { sealedTypes(it) }
        )
    )
}

private enum class Planet(val moons: Int) { Mercury(0), Earth(1), Mars(2) }

private sealed interface Payment
private data class Card(val last4: String) : Payment
private data class Cash(val amount: Int) : Payment
private data object Voucher : Payment

private fun classesAndProperties(log: SampleLog) {
    class Account(val owner: String, balance: Int) {
        var balance = balance
            private set
        fun deposit(amount: Int) {
            require(amount > 0) { "amount must be positive" }
            balance += amount
        }
    }
    val account = Account("Ada", 100)
    account.deposit(50)
    log("${account.owner}: ${account.balance}")
}

private fun dataClasses(log: SampleLog) {
    data class Point(val x: Int, val y: Int)
    val a = Point(1, 2)
    val b = Point(1, 2)
    val moved = a.copy(x = 9)
    log("a == b: ${a == b}")
    log("a === b: ${a === b}")
    log("moved: $moved")
    val (x, y) = moved
    log("x = $x, y = $y")
}

private fun copiesAndReferences(log: SampleLog) {
    data class Settings(val volume: Int)
    class Box(var volume: Int)
    val original = Settings(0)
    val copy = original.copy(volume = 99)
    log("data copy: original ${original.volume}, copy ${copy.volume}")
    val box = Box(0)
    val sameBox = box
    sameBox.volume = 99
    log("class: original ${box.volume}, alias ${sameBox.volume}")
    log("same instance: ${box === sameBox}")
}

private fun enums(log: SampleLog) {
    for (planet in Planet.entries) {
        val description = when (planet) {
            Planet.Mercury -> "too hot"
            Planet.Earth -> "home"
            Planet.Mars -> "next stop"
        }
        log("${planet.name}: ${planet.moons} moons, $description")
    }
}

private fun sealedTypes(log: SampleLog) {
    fun describe(payment: Payment) = when (payment) {   // no else needed
        is Card -> "card ending ${payment.last4}"
        is Cash -> "cash ${payment.amount}"
        Voucher -> "voucher"
    }
    listOf(Card("1234"), Cash(20), Voucher).forEach { log(describe(it)) }
}
