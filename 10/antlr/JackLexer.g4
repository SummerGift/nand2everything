lexer grammar JackLexer;

Keyword:
	'class'
	| 'constructor'
	| 'function'
	| 'method'
	| 'field'
	| 'static'
	| 'var'
	| 'int'
	| 'char'
	| 'boolean'
	| 'void'
	| 'true'
	| 'false'
	| 'null'
	| 'this'
	| 'let'
	| 'do'
	| 'if'
	| 'else'
	| 'while'
	| 'return';

IntegerConstant: [0-9]+;

StringConstant: '"' .*? '"'; 

Symbol:
	'{'
	| '}'
	| '('
	| ')'
	| '['
	| ']'
	| '.'
	| ','
	| ';'
	| '+'
	| '-'
	| '*'
	| '/'
	| '&'
	| '|'
	| '<'
	| '>'
	| '='
	| '~';

WS: [ \t\r\n\u000C]+ -> skip;
COMMENT: '/*' .*? '*/' -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;

Identifier: Letter LetterOrDigit*;

fragment LetterOrDigit: Letter | [0-9];

fragment Letter:
	[a-zA-Z$_] // these are the "java letters" below 0x7F
	| ~[\u0000-\u007F\uD800-\uDBFF] // covers all characters above 0x7F which are not a surrogate
	| [\uD800-\uDBFF] [\uDC00-\uDFFF];
