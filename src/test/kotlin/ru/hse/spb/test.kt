package ru.hse.spb

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.hse.spb.interpreter.InterpretationException


class TestSource : AbstractTester() {
    @Test
    fun testOneFunctionAndWhile() {
        interpretFile("${testDir}FunAndWhile.txt")
        val answer = "1 1$sep" +
                "2 2$sep" +
                "3 3$sep" +
                "4 5$sep" +
                "5 8$sep"
        assertEquals(answer, outContent.toString())
    }

    @Test
    fun testIfStatement() {
        interpretFile("${testDir}IfStatement.txt")
        val answer = "0$sep"
        assertEquals(answer, outContent.toString())
    }

    @Test
    fun testTwoFunctions() {
        interpretFile("${testDir}TwoFunctions.txt")
        val answer = "42$sep"
        assertEquals(answer, outContent.toString())
    }

    @Test
    fun testComment() {
        interpretFile("${testDir}Comment.txt")
        val answer = "0$sep"
        assertEquals(answer, outContent.toString())
    }

    @Test
    fun testFunctionRedefinition() {
        interpretFile("${testDir}FunctionRedefinition.txt")
        val answer = "42$sep"
        assertEquals(answer, outContent.toString())
    }

    @Test
    fun testVariableRedefinition() {
        interpretFile("${testDir}VariableRedefinition.txt")
        val answer = "6$sep"
        assertEquals(answer, outContent.toString())
    }

    @Test
    fun testFunctionNotDeclared() {
        val message = "Calling function foo wasn`t declared."
        assertThrows<InterpretationException>({ message }, { interpretFile("${testDir}FunctionNotDeclared.txt") })

    }

    @Test
    fun testVariableNotDeclared() {
        val message = "Variable k wasn`t declared."
        assertThrows<InterpretationException>({ message }, { interpretFile("${testDir}VariableNotDeclared.txt") })
    }

    @Test
    fun testZeroArguments() {
        val message = "Invalid number of arguments.$sep"
        main(arrayOf())
        assertEquals(message, outContent.toString())
    }
}