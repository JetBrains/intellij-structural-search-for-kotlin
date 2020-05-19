package com.jetbrains.kotlin.structuralsearch

class KotlinSSObjectDeclarationTest : KotlinSSTest() {
    override fun getBasePath(): String = "objectDeclaration"

    fun testObject() { doTest("object '_") }

    fun testCompanionObject() { doTest("object A") }

    fun testNestedObject() { doTest("object B") }
}