lexer grammar Jack;

//关键字
Class: 'class';
Constructot: 'constructor';
Function: 'function';
Method: 'method';
Field: 'field';
Static: 'static';
Var: 'var';
True: 'true';
False: 'false';
Null: 'null';
This: 'this';
Let: 'let';
Do: 'do';
If: 'if';
Else: 'else';
While: 'while';
Return: 'return';

//字面量
IntLiteral: [0-9]+;
StringLiteral: '"' .*? '"'; //字符串字面量

//操作符
LeftBrace: '{';
RightBrace: '}';
LeftParen: '(';
RightParen: ')';
LeftBracket: '[';
RightBracket: ']';
Dot: '.';
Comm: ',';
SemiColon: ';';
Plus: '+';
Min: '-';
Star: '*';
DIV:                '/';
BITAND:             '&';
BITOR:              '|';
LT:                 '<';
GT:                 '>';
ASSIGN:             '=';
TILDE:              '~';

// Whitespace and comments
WS:                 [ \t\r\n\u000C]+ -> channel(HIDDEN);
COMMENT:            '/*' .*? '*/'    -> channel(HIDDEN);
LINE_COMMENT:       '//' ~[\r\n]*    -> channel(HIDDEN);

// Identifier
IDENTIFIER:         Letter LetterOrDigit*;

// Fragment rules
fragment LetterOrDigit
    : Letter
    | [0-9]
    ;

fragment Letter
    : [a-zA-Z$_] // these are the "java letters" below 0x7F
    | ~[\u0000-\u007F\uD800-\uDBFF] // covers all characters above 0x7F which are not a surrogate
    | [\uD800-\uDBFF] [\uDC00-\uDFFF] // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
    ;
