package com.jetbrains.kotlin.structuralsearch

class KotlinSSParenthesesTest : KotlinSSTest() {
    override fun getBasePath(): String = "parentheses"

    fun testPostIncr() { doTest("++(('_))") }

    fun testStringLiteral() { doTest("(((\"Hello World!\")))") }

    fun testVariableRef() { doTest("(('_))") }
}