package ru.hse.spb.ast

data class Ast(val rootNode: Node) {
    interface Node {
        fun <T> accept(visitor: AstVisitor<T>): T
    }

    data class Block(val statements: List<Node>) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T = visitor.visitBlock(this)
    }

    data class Function(
            val identifier: Identifier,
            val parameterNames: ParameterNames,
            val body: Block
    ) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T = visitor.visitFunction(this)
    }

    data class Variable(
            val identifier: Identifier,
            val expression: Node?
    ) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T = visitor.visitVariable(this)
    }

    data class ParameterNames(val params: List<Identifier>) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T =
                visitor.visitParameterNames(this)
    }

    data class WhileBlock(val condition: Node, val body: Block) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T = visitor.visitWhileBlock(this)
    }

    data class IfStatement(
            val condition: Node,
            val body: Block,
            val elseBody: Block?
    ) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T = visitor.visitIfStatement(this)
    }

    data class Assignment(
            val identifier: Identifier,
            val expression: Node
    ) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T = visitor.visitAssignment(this)
    }

    data class ReturnStatement(val expression: Node) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T =
                visitor.visitReturnStatement(this)
    }

    data class FunctionCall(
            val identifier: Identifier,
            val arguments: Arguments
    ) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T = visitor.visitFunctionCall(this)
    }

    data class Arguments(val expressions: List<Node>) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T = visitor.visitArguments(this)
    }

    data class BinaryExpression(
            val leftExpression: Node,
            val operator: Operator,
            val rightExpression: Node
    ) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T =
                visitor.visitBinaryExpression(this)
    }

    data class Identifier(
            val name: String
    ) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T = visitor.visitIdentifier(this)
    }

    data class Literal(
            val literal: String
    ) : Node {
        override fun <T> accept(visitor: AstVisitor<T>): T = visitor.visitLiteral(this)
    }

    enum class Operator(val symbol: String) {
        MULTIPLY("*"),
        DIVIDE("/"),
        MODULUS("%"),
        PLUS("+"),
        MINUS("-"),
        GT(">"),
        LT("<"),
        GTE(">="),
        LTE("<="),
        EQ("=="),
        NQ("!="),
        LOR("||"),
        LAND("&&");

        companion object {
            fun getForSymbol(symbol: String): Operator? =
                    values().firstOrNull { it.symbol == symbol }
        }
    }
}