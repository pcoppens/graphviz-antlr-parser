lexer grammar words;
// lexer rules

// Comments -> ignored
COMMENT: '/*' .*? '*/' -> skip;
COMMENT2: '//' .*? NEWLINE -> skip;
// Whitespaces -> ignored
NEWLINE: '\r'? '\n'  -> skip ;

STRICT : 'strict';
GRAPH: 'graph';
DIGRAPH: 'digraph';
SUBGRAPH: 'subgraph';
NODE: 'node';
EDGE: 'edge';
UNDERSCORE: '_';
N: 'n';
E: 'e';
S: 's';
W: 'w';
C: 'c';

HTML: '<'.*? '>';
DOUBLE_QUOTED: '"'.*? '"';
NUMERAL:('-')?('.'(DIGIT)+ | (DIGIT)+ ('.'(DIGIT)*)? );
STRING: (LETTER|ULETTER|UNDERSCORE) (LETTER|ULETTER|UNDERSCORE | DIGIT)* ;

fragment LETTER: [a-zA-Z] ;
fragment DIGIT: '0'..'9' ;
fragment ULETTER: [\u200D - \u300D];