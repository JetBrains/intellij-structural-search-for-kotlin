package com.jetbrains.kotlin.structuralsearch.res

import com.jetbrains.kotlin.structuralsearch.KotlinSSResourceInspectionTest

class KotlinSSDestructuringDeclarationTest : KotlinSSResourceInspectionTest() {
    override fun getBasePath(): String = "destructuringDeclaration"

    fun testDataClass() { doTest("val ('_, '_, '_) = '_") }

    fun testLoop() { doTest("for (('_, '_) in '_) { '_* }") }

}