# OnePieceGraphviz

Solución (**sin optimizar**) al problema "One Piece" usando BFS y visualizando el grafo resultante con [Graphviz-java](https://github.com/nidi3/graphviz-java)

## Pre requisitos
Tener instalado lo siguiente:
 - [Graphviz](https://graphviz.org/download/)
 - JDK 11
 - Maven

## El problema

Se quiere encontrar el número de pasos para ir de una isla a otra, para esto la entrada consistirá en el número de
ciudades conectadas (n), en la siguiente línea van a aparecer el número de conexiones entre las ciudades y el número de
consultas entre las ciudades. <br/>
Cada ciudad empieza por una letra la cual no podrá ser repetida con otras ciudades. <br/>
La salida debe mostrar los lugares donde hay que pasar para llegar a la ciudad.<br/>
Se dará una bonificación, por mostrar el grafo y el camino necesario para avanzar.<br/>

## Ejemplo Entrada
8<br/>
7 3<br/>
Transilvania Medellin <br/>
Medellin Venice <br/>
Medellin Genoa<br/>
Transilvania Pisa <br/>
Pisa Florencia <br/>
Venice Athens <br/>
Medellin Bogota<br/>
Medellin Pisa <br/>
Bogota Florencia <br/>
Athens Genoa<br/>

## Ejemplo Salida
MedellinTransilvaniaPisa<br/>
BogotaMedellinTransilvaniaPisaFlorencia<br/>
AthensVeniceMedellinGenoa<br/>

## Ejemplo de Grafo resultante
![Medellin -> Pisa](https://github.com/devreagi/OnePieceGraphviz/blob/aa1eb9fb73a2d38b5f8ee9d9fc06d5ca5f60f186/resultado/onepiece0.png)
![Bogotá -> Florencia](https://github.com/devreagi/OnePieceGraphviz/blob/aa1eb9fb73a2d38b5f8ee9d9fc06d5ca5f60f186/resultado/onepiece1.png)
![Athens -> Genoa](https://github.com/devreagi/OnePieceGraphviz/blob/aa1eb9fb73a2d38b5f8ee9d9fc06d5ca5f60f186/resultado/onepiece2.png)
