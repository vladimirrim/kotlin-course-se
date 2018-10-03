package ru.hse.spb.interpreter

import ru.hse.spb.ast.Ast
import ru.hse.spb.ast.Ast.Operator
import ru.hse.spb.ast.AstVisitor
import java.io.PrintStream

class AstInterpreter(private val printStream: PrintStream) : AstVisitor<AstInterpreter.InterpretationResult> {

    private val context = Context(setOf(PRINTLN_IDENTIFIER))

    fun interpretAst(ast: Ast): InterpretationResult = visit(ast.rootNode)

    override fun visitBlock(block: Ast.Block): InterpretationResult {
        context.enterScope()
        for (statement in block.statements) {
            val interpretedStatement = visit(statement)
            if (interpretedStatement.shouldReturn) {
                context.leaveScope()
                return interpretedStatement
            }
        }
        context.leaveScope()
        return UNIT_INTERPRETATION
    }

    override fun visitFunction(function: Ast.Function): InterpretationResult {
        context.declareFunction(function)
        return UNIT_INTERPRETATION
    }

    override fun visitVariable(variable: Ast.Variable): InterpretationResult {
        val expression = variable.expression
        if (expression != null) {
            val interpretedExpression = visit(expression)
            context.declareVariable(variable.identifier, interpretedExpression.value!!)
        } else {
            context.declareVariable(variable.identifier, null)
        }
        return UNIT_INTERPRETATION
    }

    override fun visitParameterNames(parameterNames: Ast.ParameterNames): InterpretationResult =
            UNIT_INTERPRETATION

    override fun visitWhileBlock(whileBlock: Ast.WhileBlock): InterpretationResult {
        while (visit(whileBlock.condition).value!! != 0) {
            val interpretedBody = visit(whileBlock.body)
            if (interpretedBody.shouldReturn) {
                return interpretedBody
            }
        }
        return UNIT_INTERPRETATION
    }

    override fun visitIfStatement(ifStatement: Ast.IfStatement): InterpretationResult = when {
        visit(ifStatement.condition).value!! != 0 -> visit(ifStatement.body)
        ifStatement.elseBody != null -> visit(ifStatement.elseBody)
        else -> UNIT_INTERPRETATION
    }

    override fun visitAssignment(assignment: Ast.Assignment): InterpretationResult {
        val scope = context.getVariable(assignment.identifier).second
        context.setVariable(scope, assignment.identifier, visit(assignment.expression).value!!)
        return UNIT_INTERPRETATION
    }

    override fun visitReturnStatement(
            returnStatement: Ast.ReturnStatement
    ): InterpretationResult = InterpretationResult(visit(returnStatement.expression).value, true)

    override fun visitFunctionCall(functionCall: Ast.FunctionCall): InterpretationResult {
        val function = context.getFunction(
                functionCall.identifier, functionCall.arguments.expressions.size)
        val args = functionCall.arguments.expressions.map { visit(it).value!! }
        if (function == null) {
            return runBuiltInFunction(functionCall.identifier, args)
        }
        context.enterScope()
        val params = function.parameterNames.params
        params.zip(args).forEach { (param, arg) ->
            context.declareVariable(param, arg)
        }
        val interpretedBody = visit(function.body)
        context.leaveScope()
        return InterpretationResult(
                if (interpretedBody.shouldReturn) interpretedBody.value!! else DEFAULT_RESULT,
                false
        )
    }

    private fun runBuiltInFunction(
            identifier: Ast.Identifier, args: List<Int>
    ): InterpretationResult {
        if (identifier.name == PRINTLN_IDENTIFIER) {
            val argsString = args.joinToString(" ") { it.toString() }
            printStream.println(argsString)
            return InterpretationResult(DEFAULT_RESULT, false)
        }
        throw InterpretationException("Unknown built-in function " + identifier.name + ".")
    }

    override fun visitArguments(arguments: Ast.Arguments): InterpretationResult =
            UNIT_INTERPRETATION

    override fun visitBinaryExpression(
            binaryExpression: Ast.BinaryExpression
    ): InterpretationResult {
        val leftValue = visit(binaryExpression.leftExpression).value!!
        val rightValue = visit(binaryExpression.rightExpression).value!!
        val op = binaryExpression.operator
        val resultValue = when (op) {
            Operator.MULTIPLY -> leftValue * rightValue
            Operator.DIVIDE -> leftValue / rightValue
            Operator.MODULUS -> leftValue % rightValue
            Operator.PLUS -> leftValue + rightValue
            Operator.MINUS -> leftValue - rightValue
            Operator.GT -> (leftValue > rightValue).int
            Operator.LT -> (leftValue < rightValue).int
            Operator.GTE -> (leftValue >= rightValue).int
            Operator.LTE -> (leftValue <= rightValue).int
            Operator.EQ -> (leftValue == rightValue).int
            Operator.NQ -> (leftValue != rightValue).int
            Operator.LOR -> (leftValue.bool || rightValue.bool).int
            Operator.LAND -> (leftValue.bool && rightValue.bool).int
        }
        return InterpretationResult(resultValue, false)
    }

    override fun visitIdentifier(identifier: Ast.Identifier): InterpretationResult =
            InterpretationResult(context.getVariable(identifier).first, false)

    override fun visitLiteral(number: Ast.Literal): InterpretationResult {
        val intValue = number.literal.toInt()
        return InterpretationResult(intValue, false)
    }

    data class InterpretationResult(val value: Int?, val shouldReturn: Boolean)

    companion object {
        private val UNIT_INTERPRETATION = InterpretationResult(null, false)
        private const val DEFAULT_RESULT = 0
        private const val PRINTLN_IDENTIFIER = "println"

        val Boolean.int
            get() = if (this) 1 else 0

        val Int.bool
            get() = this != 0
    }
}