grammar Data;

WS: [ \t\r\n]+ -> skip; // skip spaces, tabs, newlines

calc: expr EOF;
expr: ('(' expr_name attrs expr* ')') |
      ('(' expr_name expr* ')') |
      value;
expr_name: NAME;
attrs: '{' (attr )* attr '}';
attr: attr_name '=' STRING;
attr_name: NAME;
value: STRING | INT | DOUBLE;

NAME: [A-Za-z] [A-Za-z0-9_]*;



STRING
    : '"' (ESC | SAFECODEPOINT)* '"'
    ;

fragment ESC
    : '\\' (["\\/bfnrt] | UNICODE)
    ;
fragment UNICODE
    : 'u' HEX HEX HEX HEX
    ;
fragment HEX
    : [0-9a-fA-F]
    ;
fragment SAFECODEPOINT
    : ~ ["\\\u0000-\u001F]
    ;
DOUBLE
    : INT ('.' [0-9]+) EXP?
    ;
INT
    : '-'? INT_NO_SIGN;
fragment INT_NO_SIGN
    // integer part forbis leading 0s (e.g. `01`)
    : '0'
    | [1-9] [0-9]*
    ;
// no leading zeros
fragment EXP
    // exponent number permits leading 0s (e.g. `1e01`)
    : [Ee] [+\-]? [0-9]+
    ;
