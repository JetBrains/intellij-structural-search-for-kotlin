package com.jetbrains.kotlin.structuralsearch.res

import com.jetbrains.kotlin.structuralsearch.KotlinSSResourceInspectionTest

class KotlinSSIfExpressionTest : KotlinSSResourceInspectionTest() {
    override fun getBasePath(): String = "ifExpression"

    fun testIf() { doTest("if(true) b = true") }

    fun testIfElse() { doTest("if(true) 1 else 2") }

    fun testIfBlock() {
        doTest(
            """
            if(true) {
|               a = 1
|           }""".trimMargin()
        )
    }

    fun testIfElseBlock() {
        doTest(
            """
            if (a == 1) {
                a = 2
            } else {
                a = 3
            }""".trimMargin()
        )
    }

    fun testIfElseCondition() {
        doTest(
            """
            if (a == 1) {
                a = 2
            } else {
                a = 3
            }""".trimMargin()
        )
    }
}