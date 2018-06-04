lexer grammar words;
// lexer rules

// Comments -> ignored
COMMENTS        : ('/*' .*? '*/' | '//' ~'\n'* '\n' ) -> skip;
// Whitespaces -> ignored
NEWLINE: '\r'? '\n'  -> skip ;
WS: [ \t]+ -> skip;

STRICT : 'strict';
GRAPH: 'graph';
DIGRAPH: 'digraph';
SUBGRAPH: 'subgraph';
NODE: 'node';
EDGE: 'edge';
UNDERSCORE: '_';
OPEN: '{';
CLOSE: '}';
SEMICOLON: ';';
COMMA: ',';
DBLPOINT: ':';
DASH:'--';
DDASH:'->';
N: 'n';
E: 'e';
NE: 'ne';
S: 's';
SE: 'se';
W: 'w';
SW: 'sw';
NW: 'nw';
C: 'c';

HTML: '<'.*? '>';
DOUBLE_QUOTED: '"'.*? '"';
NUMERAL:('-')?('.'(DIGIT)+ | (DIGIT)+ ('.'(DIGIT)*)? );
STRING: (LETTER|UNDERSCORE) (LETTER|UNDERSCORE | DIGIT)* ;

fragment LETTER: 'A'..'Z' | 'a'..'z' ;
fragment DIGIT: '0'..'9' ;
//fragment ULETTER: [\u200D - \u300D];

// handle characters which failed to match any other token
ErrorCharacter : . ;