package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.ast.AstBuilder
import ru.hse.spb.interpreter.AstInterpreter
import ru.hse.spb.interpreter.InterpretationException
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser
import ru.hse.spb.parser.ParserException


fun interpretFile(filename: String) {
    val lexer = ExpLexer(CharStreams.fromFileName(filename))
    val parser = ExpParser(BufferedTokenStream(lexer))
    val builder = AstBuilder()
    val parseTree = parser.block()
    if (parser.numberOfSyntaxErrors > 0)
        throw ParserException("Invalid syntax.")
    AstInterpreter(System.out).interpretAst(builder.buildAstFromContext(parseTree))
}

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Invalid number of arguments.")
        return
    }
    try {
        interpretFile(args[0])
    } catch (e: ParserException) {
        println("An error occurred while parsing file:")
        println(e.localizedMessage)
    } catch (e: InterpretationException) {
        println("An error occurred while interpreting file:")
        println(e.localizedMessage)
    }
}