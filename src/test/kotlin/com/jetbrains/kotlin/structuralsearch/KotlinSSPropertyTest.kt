package com.jetbrains.kotlin.structuralsearch

class KotlinSSPropertyTest : KotlinSSTest() {
    override fun getBasePath() = "property"

    fun testVar() { doTest("var '_") }

    fun testVal() { doTest("val '_") }

    fun testValType() { doTest("val '_ : Int") }

    fun testValFqType() { doTest("val '_ : Foo.Int") }

    fun testValComplexFqType() { doTest("val '_ : '_<'_<'_, (Foo.Int) -> Int>>") }

    fun testValInitializer() { doTest("val '_ = 1") }

    fun testValReceiverType() { doTest("val '_ : ('_T) -> '_U = '_") }

    fun testVarTypeProjection() { doTest("var '_ : Comparable<'_T>") }
}