package com.jetbrains.kotlin.structuralsearch.search

import com.jetbrains.kotlin.structuralsearch.KotlinSSResourceInspectionTest

class KotlinSSDoubleColonExpressionTest : KotlinSSResourceInspectionTest() {
    override fun getBasePath(): String = "doubleColonExpression"

    fun testClassLiteralExpression() { doTest("Int::class") }

    fun testFqClassLiteralExpression() { doTest("kotlin.Int::class") }

    fun testDotQualifiedExpression() { doTest("Int::class.java") }
}