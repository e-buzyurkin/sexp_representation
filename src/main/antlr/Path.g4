grammar Path;

calc
    : locationPath EOF
    ;

locationPath
    : relativeLocationPath
    | absoluteLocationPathNoroot
    ;

absoluteLocationPathNoroot
    : '/' relativeLocationPath
    | '//' relativeLocationPath
    ;

relativeLocationPath
    : step (stepSeparator step)*
    ;

stepSeparator
    : '/'
    | '//'
    ;

type
    : '@element'
    | '@value'
    ;

step
    : nCName predicate?
    | abbreviatedStep predicate?
    | type predicate?
    ;



predicate
    : '[' expr ']'
    ;

abbreviatedStep
    : '.'
    | '..'
    ;

expr
    : equal_pred_expr
    | custom_predicat_expr
    ;

equal_pred_expr:
    attribute_expr ('=' | '!=') attribute_expr
    ;

custom_predicat_expr:
    NAME '('  attribute_expr ',' attribute_expr ')'
    ;

attribute_expr:
    '@' nCName
    | STRING
    ;

nCName
    : NAME
    | '*'
    ;

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

NAME: [A-Za-z] [A-Za-z0-9_]*;


Whitespace
    : (' ' | '\t' | '\n' | '\r')+ -> skip
    ;