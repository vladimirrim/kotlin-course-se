package ru.hse.spb.parser

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.hse.spb.AbstractTester

class ParserTest : AbstractTester() {
    @Test
    fun missingBrace() {
        val lexer = ExpLexer(CharStreams.fromFileName("${testDir}MissingBrace.txt"))
        val parser = ExpParser(BufferedTokenStream(lexer))
        parser.block()
        val answer = "line 6:14 missing '}' at '<EOF>'$sep"
        assertEquals(answer, errContent.toString())
    }

    @Test
    fun testOneFunctionAndWhile() {
        val lexer = ExpLexer(CharStreams.fromFileName("${testDir}FunAndWhile.txt"))
        val parser = ExpParser(BufferedTokenStream(lexer))
        val answer = "(block (statement (function fun fib ( (parameterNames n) ) (blockWithBraces { (block (statement (ifExpr if ( (expression (binaryExpression (expressionVariable n) <= (expression (expressionVariable 1)))) ) (blockWithBraces { (block (statement (returnExpr return (expression (expressionVariable 1))))) }))) (statement (returnExpr return (expression (binaryExpression (expressionVariable (functionCall fib ( (arguments (expression (binaryExpression (expressionVariable n) - (expression (expressionVariable 1))))) ))) + (expression (expressionVariable (functionCall fib ( (arguments (expression (binaryExpression (expressionVariable n) - (expression (expressionVariable 2))))) ))))))))) }))) (statement (variable var i = (expression (expressionVariable 1)))) (statement (whileExpr while ( (expression (binaryExpression (expressionVariable i) <= (expression (expressionVariable 5)))) ) (blockWithBraces { (block (statement (expression (expressionVariable (functionCall println ( (arguments (expression (expressionVariable i)) , (expression (expressionVariable (functionCall fib ( (arguments (expression (expressionVariable i))) ))))) ))))) (statement (assignment i = (expression (binaryExpression (expressionVariable i) + (expression (expressionVariable 1))))))) }))))"
        assertEquals(answer, parser.block().toStringTree(parser))
    }

    @Test
    fun testIfStatement() {
        val lexer = ExpLexer(CharStreams.fromFileName("${testDir}IfStatement.txt"))
        val parser = ExpParser(BufferedTokenStream(lexer))
        val answer = "(block (statement (variable var a = (expression (expressionVariable 10)))) (statement (variable var b = (expression (expressionVariable 20)))) (statement (ifExpr if ( (expression (binaryExpression (expressionVariable a) > (expression (expressionVariable b)))) ) (blockWithBraces { (block (statement (expression (expressionVariable (functionCall println ( (arguments (expression (expressionVariable 1))) )))))) }) else (blockWithBraces { (block (statement (expression (expressionVariable (functionCall println ( (arguments (expression (expressionVariable 0))) )))))) }))))"
        assertEquals(answer, parser.block().toStringTree(parser))
    }

    @Test
    fun testTwoFunctions() {
        val lexer = ExpLexer(CharStreams.fromFileName("${testDir}TwoFunctions.txt"))
        val parser = ExpParser(BufferedTokenStream(lexer))
        val answer = "(block (statement (function fun foo ( (parameterNames n) ) (blockWithBraces { (block (statement (function fun bar ( (parameterNames m) ) (blockWithBraces { (block (statement (returnExpr return (expression (binaryExpression (expressionVariable m) + (expression (expressionVariable n))))))) }))) (statement (returnExpr return (expression (expressionVariable (functionCall bar ( (arguments (expression (expressionVariable 1))) ))))))) }))) (statement (expression (expressionVariable (functionCall println ( (arguments (expression (expressionVariable (functionCall foo ( (arguments (expression (expressionVariable 41))) ))))) ))))))"
        assertEquals(answer, parser.block().toStringTree(parser))
    }
}