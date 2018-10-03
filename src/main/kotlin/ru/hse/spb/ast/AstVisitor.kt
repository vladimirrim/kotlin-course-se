package ru.hse.spb.ast

interface AstVisitor<out T> {
    fun visit(node: Ast.Node): T {
        return node.accept(this)
    }

    fun visitBlock(block: Ast.Block): T

    fun visitWhileBlock(whileBlock: Ast.WhileBlock): T

    fun visitIfStatement(ifStatement: Ast.IfStatement): T

    fun visitFunction(function: Ast.Function): T

    fun visitVariable(variable: Ast.Variable): T

    fun visitBinaryExpression(binaryExpression: Ast.BinaryExpression): T

    fun visitArguments(arguments: Ast.Arguments): T

    fun visitParameterNames(parameterNames: Ast.ParameterNames): T

    fun visitAssignment(assignment: Ast.Assignment): T

    fun visitReturnStatement(returnStatement: Ast.ReturnStatement): T

    fun visitFunctionCall(functionCall: Ast.FunctionCall): T

    fun visitIdentifier(identifier: Ast.Identifier): T

    fun visitLiteral(number: Ast.Literal): T
}