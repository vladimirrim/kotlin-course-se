package ru.hse.spb.ast

import ru.hse.spb.ast.Ast.*
import ru.hse.spb.ast.Ast.Function
import ru.hse.spb.ast.Ast.Operator.*

object Asts {
    val AST_IF_STATEMENT = Ast(
            Block(listOf(
                    Variable(Identifier("a"), Literal("10")),
                    Variable(Identifier("b"), Literal("20")),
                    IfStatement(
                            BinaryExpression(Identifier("a"), GT, Identifier("b")),
                            Block(listOf(
                                    FunctionCall(
                                            Identifier("println"),
                                            Arguments(listOf(Literal("1")))))),
                            Block(listOf(
                                    FunctionCall(
                                            Identifier("println"),
                                            Arguments(listOf(Literal("0"))))))))
            )
    )

    val AST_FUN_AND_WHILE = Ast(
            Block(listOf(
                    Function(
                            Identifier("fib"),
                            ParameterNames(listOf(Identifier("n"))),
                            Block(listOf(
                                    IfStatement(
                                            BinaryExpression(Identifier("n"), LTE, Literal("1")),
                                            Block(listOf(
                                                    ReturnStatement(Literal("1")))),
                                            null),
                                    ReturnStatement(
                                            BinaryExpression(
                                                    FunctionCall(
                                                            Identifier("fib"),
                                                            Arguments(listOf(BinaryExpression(
                                                                    Identifier("n"), MINUS, Literal("1"))))),
                                                    PLUS,
                                                    FunctionCall(
                                                            Identifier("fib"),
                                                            Arguments(listOf(BinaryExpression(
                                                                    Identifier("n"), MINUS, Literal("2")))))))
                            ))
                    ),
                    Variable(Identifier("i"), Literal("1")),
                    WhileBlock(
                            BinaryExpression(Identifier("i"), LTE, Literal("5")),
                            Block(listOf(
                                    FunctionCall(
                                            Identifier("println"),
                                            Arguments(listOf(
                                                    Identifier("i"),
                                                    FunctionCall(
                                                            Identifier("fib"),
                                                            Arguments(listOf(Identifier("i"))))))),
                                    Assignment(
                                            Identifier("i"),
                                            BinaryExpression(Identifier("i"), PLUS, Literal("1")))
                            ))
                    )
            ))
    )

    val AST_TWO_FUNCTIONS = Ast(
            Block(listOf(
                    Function(
                            Identifier("foo"),
                            ParameterNames(listOf(Identifier("n"))),
                            Block(listOf(
                                    Function(
                                            Identifier("bar"),
                                            ParameterNames(listOf(Identifier("m"))),
                                            Block(listOf(
                                                    ReturnStatement(
                                                            BinaryExpression(
                                                                    Identifier("m"), PLUS, Identifier("n")))))
                                    ),
                                    ReturnStatement(
                                            FunctionCall(
                                                    Identifier("bar"),
                                                    Arguments(listOf(Literal("1"))))
                                    )
                            ))
                    ),
                    FunctionCall(
                            Identifier("println"),
                            Arguments(listOf(
                                    FunctionCall(
                                            Identifier("foo"),
                                            Arguments(listOf(Literal("41"))))))
                    )
            ))
    )
}