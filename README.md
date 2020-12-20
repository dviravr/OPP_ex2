# Directed Weighted Graph

An implementation of directed weighted graph in java

### Node Data class

* NodeData hold node's information
key, location, tag, info and weight.

* There is a getters and setters for all but the key for him there is just a getter.

### Edge Data class

* EdgeData hold edge's information
src node key, dest node key, tag, info and weight.

* There is a getters for all and setter for the tag and info.

### Geo Location class

* GeoLocation is a 3D point. Every point have x, y, and z.

* `distance()` return the distance between two points

### Directed Weighted Graph class

* WGraph_DS holds two hashmaps.
the first is for the nodes - the key is the key of the node, 
and the value is the node himself.
the second is for the edges - the key is a node key,
and the value is a hashmap of all his neighbors.

* `addNode()` add a given node to the graph
`getNode()` return the node with given key

* `getEdge()` return the edge between two givens nodes.

* `connect()` make a new edge between two nodes with a given weight.
if there is an edge already the method simply update the weight.

* `removeEdge()` simply delete the edge between two givens nodes.
`removeNode()` delete the node from the graph and delete all the edges that connected to him 
by using `removeEdge()`.

* `getV()` the method simply return a collection of all the nodes in the graph.
`getE(key)` return a collection of all the neighbors of a specific node.

* `nodeSize()` return the number of nodes in the graph

* `edgeSize()` return the number of the edges in the graph

### Directed Weighted Graph Algorithm class

* WGraph_Algo holds a graph, all the algorithms works on the graph.

* there are two constructors, one without a given graph and one with a given graph.

* the method `init()` change the class's graph to a given graph.
`getGraph()` return the class's graph.

* `copy()` make a deep copy of the class's graph and return him.

* `isConnected()` return true if the graph is strongly connected and false if not.

* the method `shortestPathDist()` return the shortest path distance between two givens nodes.
 the method `shortestPath()` return a list of the shortest path between two givens nodes.
 
* the method `save()` simply save the class's graph to given file in json format.
 the method `load()` simply load a graph to the class's graph from a given json file.
  

#### Adapters classes

* for the NodeData, EdgeData and WGraph_DS there are serialize to json format and deserializers from json format

* NodeData format: { "pos": "x,y,z", "id": int }

* EdgeData format: { "src": int, "w": double, "dest": int }

* WGraph_DS format: { "Edges: [edge, edge, edge]", "Nodes": [node, node, node] }

## How to run the program

* First you need to clone the project to your computer: `git clone https://github.com/dviravr/OPP_ex2.git`.

* Now you can choose to run it from the main or the jar file.
 to run it from the jar file you need to open your CMD in the directory, and run the command: `java -jar Ex2.jar`
 you can add an scenerio and id as param to the command like this: `java -jar Ex2.jar 123456789 12`
 
 * The program require jdk 13 and up.
