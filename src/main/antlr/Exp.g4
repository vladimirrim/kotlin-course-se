grammar Exp;

block
    : (statement)*
    ;

blockWithBraces
    : '{' block '}'
    ;

statement
    : function
    | variable
    | expression
    | whileExpr
    | ifExpr
    | assignment
    | returnExpr
    ;

function
    : 'fun' Identifier '(' parameterNames ')' blockWithBraces
    ;

variable
    : 'var' Identifier ('=' expression)?
    ;

parameterNames
    : (Identifier (',' Identifier)*)?
    ;

whileExpr
    : 'while' '(' expression ')' blockWithBraces
    ;

ifExpr
    : 'if' '(' expression ')' blockWithBraces ('else' blockWithBraces)?
    ;

assignment
    : Identifier '=' expression
    ;

returnExpr
    : 'return' expression
    ;

functionCall
    : Identifier '(' arguments ')'
    ;

arguments
    : (expression (',' expression)*)?
    ;

expression
    : binaryExpression
    | expressionVariable
    ;

expressionVariable
    : functionCall
    | Literal
    | Identifier
    | '(' expression ')'
    ;

binaryExpression
    : expressionVariable op = (MULTIPLY | DIVIDE | MODULUS) expression
    | expressionVariable op = (PLUS | MINUS) expression
    | expressionVariable op = (GT | LT | GTE | LTE) expression
    | expressionVariable op = (EQ | NQ) expression
    | expressionVariable op = LAND expression
    | expressionVariable op = LOR expression
    ;

MULTIPLY : '*';
DIVIDE : '/';
MODULUS : '%';

PLUS : '+';
MINUS : '-';

GT : '>';
LT : '<';
GTE : '>=';
LTE : '<=';

EQ : '==';
NQ : '!=';

LOR : '||';
LAND : '&&';

Literal
    : '0'
    | [1-9] ([0-9])*
    ;

Identifier
    : [a-zA-Z_]
      ([a-zA-Z_0-9])*
    ;

COMMENT : '//' ~[\r\n]* -> skip;

WS : (' ' | '\t' | '\r'| '\n') -> skip;