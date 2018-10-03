package ru.hse.spb.ast

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree
import ru.hse.spb.parser.ExpBaseVisitor
import ru.hse.spb.parser.ExpParser

class AstBuilder : ExpBaseVisitor<Ast.Node>() {
    fun buildAstFromContext(ctx: ParserRuleContext): Ast {
        val rootNode = visit(ctx)
        return Ast(rootNode)
    }

    override fun visitBlock(ctx: ExpParser.BlockContext): Ast.Node {
        val statements = ctx.statement().map { visit(it) as Ast.Node }
        return Ast.Block(statements.toList())
    }

    override fun visitFunction(ctx: ExpParser.FunctionContext): Ast.Node {
        val identifier = Ast.Identifier(ctx.Identifier().text)
        val paramNames = visit(ctx.parameterNames()) as Ast.ParameterNames
        val body = visit(ctx.blockWithBraces()) as Ast.Block
        return Ast.Function(identifier, paramNames, body)
    }

    override fun visitVariable(ctx: ExpParser.VariableContext): Ast.Node {
        val identifier = Ast.Identifier(ctx.Identifier().text)
        val possibleExpression = ctx.expression()
        val expression = possibleExpression?.let { visit(it) }
        return Ast.Variable(identifier, expression)
    }

    override fun visitParameterNames(ctx: ExpParser.ParameterNamesContext): Ast.Node {
        val identifiers = ctx.Identifier()
        val params = identifiers?.map { Ast.Identifier(it.text) }.orEmpty()
        return Ast.ParameterNames(params)
    }

    override fun visitWhileExpr(ctx: ExpParser.WhileExprContext): Ast.Node {
        val condition = visit(ctx.expression())
        val body = visit(ctx.blockWithBraces()) as Ast.Block
        return Ast.WhileBlock(condition, body)
    }

    override fun visitIfExpr(ctx: ExpParser.IfExprContext): Ast.Node {
        val condition = visit(ctx.expression())
        val blocks = ctx.blockWithBraces().map { visit(it) }
        val body = blocks[0] as Ast.Block
        val elseBody = blocks.getOrNull(1) as Ast.Block?
        return Ast.IfStatement(condition, body, elseBody)
    }

    override fun visitAssignment(ctx: ExpParser.AssignmentContext): Ast.Node {
        val identifier = Ast.Identifier(ctx.Identifier().text)
        val expression = visit(ctx.expression())
        return Ast.Assignment(identifier, expression)
    }

    override fun visitReturnExpr(ctx: ExpParser.ReturnExprContext): Ast.Node {
        val expression = visit(ctx.expression())
        return Ast.ReturnStatement(expression)
    }

    override fun visitFunctionCall(ctx: ExpParser.FunctionCallContext): Ast.Node {
        val identifier = Ast.Identifier(ctx.Identifier().text)
        val arguments = visit(ctx.arguments()) as Ast.Arguments
        return Ast.FunctionCall(identifier, arguments)
    }

    override fun visitArguments(ctx: ExpParser.ArgumentsContext): Ast.Node {
        val possibleExpressions = ctx.expression()
        val expressions =
                possibleExpressions?.asSequence()?.map { visit(it) }?.toList() ?: listOf()
        return Ast.Arguments(expressions)
    }

    override fun visitBinaryExpression(ctx: ExpParser.BinaryExpressionContext): Ast.Node {
        val leftExpression = visit(ctx.expressionVariable())
        val operator = Ast.Operator.getForSymbol(ctx.op.text)!!
        val rightExpression = visit(ctx.expression())
        return Ast.BinaryExpression(leftExpression, operator, rightExpression)
    }

    override fun visitExpressionVariable(ctx: ExpParser.ExpressionVariableContext): Ast.Node {
        val identifier = ctx.Identifier()
        identifier?.let {
            return Ast.Identifier(it.text)
        }
        val literal = ctx.Literal()
        literal?.let { return Ast.Literal(it.text) }
        val otherExpressionVariables = listOf<ParseTree?>(
                ctx.functionCall(),
                ctx.expression()
        )
        val expressionVariable = otherExpressionVariables.find { it != null }
        return visit(expressionVariable!!)
    }

    override fun aggregateResult(aggregate: Ast.Node?, nextResult: Ast.Node?): Ast.Node? =
            aggregate ?: nextResult
}