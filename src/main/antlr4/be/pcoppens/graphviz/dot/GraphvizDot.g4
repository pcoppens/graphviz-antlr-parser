grammar GraphvizDot; //from grammar https://graphviz.gitlab.io/_pages/doc/info/lang.html
import words;
//  parser rules

graph	:	(STRICT)? (GRAPH | DIGRAPH) (id)? '{' stmt_list '}' EOF ;


stmt_list
   : ( stmt (';')? )* ;

stmt	:
	node_stmt
|	edge_stmt
|	attr_stmt
|	id '=' id
|	subgraph ;
attr_stmt	:	(GRAPH | NODE | EDGE) attr_list ;
attr_list	:	'[' (a_list)? ']' (attr_list)? ;
a_list	:	id '=' id  (';' | ',')? (a_list)? ;
edge_stmt	:	(node_id | subgraph) edgeRHS (attr_list)? ;
edgeRHS	:	edgeOp (node_id | subgraph) (edgeRHS)? ;
node_stmt	:	node_id (attr_list)? ;
node_id	:	id (port)? ;
port	:	':' id (':' compass_pt)? |	':' compass_pt ;

subgraph	:	(SUBGRAPH (id)? )? '{' stmt_list '}' ;
compass_pt	:	(N | N E | E | S E | S | S W | W | N W | C | UNDERSCORE) ;
edgeOp: '-->' | '--';
id: STRING | NUMERAL | DOUBLE_QUOTED | HTML;
