package ru.hse.spb.interpreter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.hse.spb.AbstractTester
import ru.hse.spb.ast.Asts.AST_FUN_AND_WHILE
import ru.hse.spb.ast.Asts.AST_IF_STATEMENT
import ru.hse.spb.ast.Asts.AST_TWO_FUNCTIONS

class AstInterpreterTest : AbstractTester() {
    @Test
    fun testOneFunctionAndWhile() {
        val answer = "1 1$sep" +
                "2 2$sep" +
                "3 3$sep" +
                "4 5$sep" +
                "5 8$sep"
        AstInterpreter(System.out).visit(AST_FUN_AND_WHILE.rootNode)
        assertEquals(answer, outContent.toString())
    }

    @Test
    fun testIfStatement() {
        val answer = "0$sep"
        AstInterpreter(System.out).visit(AST_IF_STATEMENT.rootNode)
        assertEquals(answer, outContent.toString())
    }

    @Test
    fun testTwoFunctions() {
        val answer = "42$sep"
        AstInterpreter(System.out).visit(AST_TWO_FUNCTIONS.rootNode)
        assertEquals(answer, outContent.toString())
    }
}