package com.jetbrains.kotlin.structuralsearch.res

import com.jetbrains.kotlin.structuralsearch.KotlinSSResourceInspectionTest

class KotlinSSFunctionTest : KotlinSSResourceInspectionTest() {
    override fun getBasePath(): String = "function"

    fun testFun() { doTest("fun a() { '_* }") }

    fun testFunAny() { doTest("fun '_( '_* )") }

    fun testFunLocal() { doTest("fun b() { '_* }") }

    fun testFunParam() { doTest("fun '_(b: Int, c: String) { '_* }") }

    fun testFunSingleParam() { doTest("fun '_('_ : '_) { '_* }") }

    fun testFunTypeParam() { doTest("fun<T, R> '_(a: T, b: R, c: T) { '_* }") }

    fun testFunReturnType() { doTest("fun '_(b: Int): Int { return b }") }

    fun testFunBlockBody() {
        doTest(
            """
            fun '_() {
                println()
            }
            """
        )
    }

    fun testFunPublicModifier() { doTest("public fun '_('_*)") }

    fun testFunInternalModifier() { doTest("internal fun '_()") }

    fun testFunPrivateModifier() { doTest("private fun '_()") }

    fun testFunTypeVarRef() { doTest("fun '_(): '_") }

    fun testFunSimpleTypeReceiver() { doTest("fun<'_type> '_('_ : '_.('_type) -> '_)") }

    fun testFunReceiverType() {
        doTest(
            "fun <'_T, '_E, '_R> '_name('_f : '_T.('_E) -> '_R) : ('_T, '_E) -> '_R = { '_t, '_e -> '_t.'_f('_e) }"
        )
    }

    fun testFunTypeParamArgs() { doTest("fun <'_E, '_T> '_name(p1: '_E, p2: '_T)") }

    fun testMethod() { doTest("fun a()") }

    fun testMethodProtectedModifier() { doTest("protected fun '_()") }

    fun testFunExprBlock() { doTest("fun '_(): Int = 0") }

    fun testFunAnnotation() { doTest("@Foo fun '_('_*)") }
}