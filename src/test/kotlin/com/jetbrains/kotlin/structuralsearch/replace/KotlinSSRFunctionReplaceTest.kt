package com.jetbrains.kotlin.structuralsearch.replace

import com.jetbrains.kotlin.structuralsearch.KotlinSSRReplaceTest

class KotlinSSRFunctionReplaceTest : KotlinSSRReplaceTest() {
    fun testVisibilityModifierCopy() {
        doTest(
            searchPattern = "fun '_ID('_PARAM*)",
            replacePattern = "fun '_ID('_PARAM)",
            match = "public fun foo() {}",
            result = "public fun foo() {}"
        )
    }

    fun testVisibilityModifierRemoval() {
        doTest(
            searchPattern = "public fun '_ID('_PARAM*)",
            replacePattern = "fun '_ID('_PARAM)",
            match = "public fun foo() {}",
            result = "fun foo() {}"
        )
    }

    fun testVisibilityModifierReplace() {
        doTest(
            searchPattern = "public fun '_ID('_PARAM*)",
            replacePattern = "private fun '_ID('_PARAM)",
            match = "public fun foo() {}",
            result = "private fun foo() {}"
        )
    }

    fun testVisibilityModifierFormatCopy() {
        doTest(
            searchPattern = "fun '_ID('_PARAM*)",
            replacePattern = "fun '_ID('_PARAM)",
            match = "public  fun foo() {}",
            result = "public  fun foo() {}"
        )
    }
    
    fun testFunctionParameterFormatCopy() {
        doTest(
            searchPattern = "fun '_ID('_PARAM)",
            replacePattern = "fun '_ID('_PARAM)",
            match = "public fun foo(bar  :  Int  =  0)  {}",
            result = "public fun foo(bar  :  Int  =  0)  {}"
        )
    }

    fun testFunctionTypedParameterFormatCopy() {
        doTest(
            searchPattern = "fun '_ID('_PARAM : '_TYPE)",
            replacePattern = "fun '_ID('_PARAM : '_TYPE)",
            match = "public fun foo(bar : Int  =  0)  {}",
            result = "public fun foo(bar : Int  =  0)  {}"
        )
    }

    fun testFunctionDefaultParameterFormatCopy() {
        doTest(
            searchPattern = "fun '_ID('_PARAM : '_TYPE = '_INIT)",
            replacePattern = "fun '_ID('_PARAM : '_TYPE = '_INIT)",
            match = "public fun foo(bar : Int = 0)  {}",
            result = "public fun foo(bar : Int = 0)  {}"
        )
    }
}