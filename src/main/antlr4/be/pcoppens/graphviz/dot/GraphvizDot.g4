grammar GraphvizDot; //from grammar https://graphviz.gitlab.io/_pages/doc/info/lang.html
import words;
//  parser rules

graph : (STRICT)? (GRAPH|DIGRAPH) (id)?	OPEN stmt_list CLOSE EOF ;

stmt_list
   : ( stmt (SEMICOLON)? )* ;

stmt	:
	node_stmt
|	edge_stmt
|	attr_stmt
|	id '=' id
|	subgraph ;
attr_stmt	:	(GRAPH | NODE | EDGE) attr_list ;
attr_list	:	'[' (a_list)? ']' (attr_list)? ;
a_list	:	id '=' id  (SEMICOLON | COMMA)? (a_list)? ;
edge_stmt	:	(node_id | subgraph) edgeRHS (attr_list)? ;
edgeRHS	:	(DASH |DDASH) (node_id | subgraph) (edgeRHS)? ;
node_stmt	:	node_id (attr_list)? ;
node_id	:	id (port)? ;
port	:	DBLPOINT id (DBLPOINT compass_pt)? | DBLPOINT compass_pt ;

subgraph	:	(SUBGRAPH (id)? )? OPEN stmt_list CLOSE ;
compass_pt	:	(N | NE | E | SE | S | SW | W | NW | C | UNDERSCORE) ;

id: STRING | NUMERAL | DOUBLE_QUOTED | HTML;

