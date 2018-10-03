package ru.hse.spb.interpreter

import ru.hse.spb.ast.Ast
import java.util.*

class Context(
        private val builtInFunctions: Set<String> = setOf(),
        private val scopeStack: java.util.Deque<Scope> = ArrayDeque()
) {
    fun enterScope() {
        scopeStack.push(Context.Scope())
    }

    fun leaveScope() {
        scopeStack.pop()
    }

    fun declareFunction(function: Ast.Function) =
            putIfNotDefined(scopeStack.peek().functions, function.identifier, function, "Function")

    fun declareVariable(identifier: Ast.Identifier, value: Int?) =
            putIfNotDefined(scopeStack.peek().variables, identifier, value, "Variable")

    private fun <T> putIfNotDefined(container: MutableMap<Ast.Identifier, T>, identifier: Ast.Identifier, value: T, type: String) {
        if (container.contains(identifier))
            throw InterpretationException("$type ${identifier.name} is redefined in the same scope.")
        else
            container[identifier] = value
    }

    fun getVariable(identifier: Ast.Identifier): Pair<Int?, Scope> {
        for (scope in scopeStack.iterator()) {
            scope.variables[identifier]?.let {
                return Pair(it, scope)
            }
        }
        throw InterpretationException("Variable ${identifier.name} wasn`t declared.")
    }

    fun setVariable(scope: Scope, identifier: Ast.Identifier, value: Int) {
        scope.variables[identifier] = value
    }

    fun getFunction(identifier: Ast.Identifier, size: Int): Ast.Function? {
        if (identifier.name in builtInFunctions)
            return null

        for (scope in scopeStack.iterator()) {
            if (scope.functions.contains(identifier)) {
                if (scope.functions[identifier]?.parameterNames?.params?.size == size) {
                    return scope.functions[identifier]
                } else
                    throw InterpretationException("Calling function ${identifier.name} has different number of parameters.")
            }
        }
        throw InterpretationException("Calling function ${identifier.name} wasn`t declared.")
    }


    data class Scope(
            val variables: MutableMap<Ast.Identifier, Int?> = mutableMapOf(),
            val functions: MutableMap<Ast.Identifier, Ast.Function> = mutableMapOf()
    )
}