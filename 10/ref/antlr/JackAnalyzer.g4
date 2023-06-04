grammar JackAnalyzer;

import JackLexer;

statement
    : statement*
    ;


class
classVarDec
subroutineDec
parameterList
subroutineBody
varDec

statements, whileStatement, ifStatement,returnStatement,letStatement,doStatement;

expression,term,expressionList;