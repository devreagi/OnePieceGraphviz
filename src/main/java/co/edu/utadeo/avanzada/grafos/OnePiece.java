package co.edu.utadeo.avanzada.grafos;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.attribute.Rank.RankDir.TOP_TO_BOTTOM;
import static guru.nidi.graphviz.model.Factory.*;

public class OnePiece {

    //Clase que representa las aristas
    public static class Edge {
        final int from;
        final int to;
        final int cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    private final int n;
    private Integer[] prev;
    private final List<List<Edge>> graph;

    public OnePiece(List<List<Edge>> graph) {
        if (graph == null) throw new IllegalArgumentException("Graph can not be null");
        n = graph.size();
        this.graph = graph;
    }

    public List<Integer> reconstructPath(int start, int end) {
        bfs(start);
        List<Integer> path = new ArrayList<>();
        for (Integer at = end; at != null; at = prev[at]) path.add(at);
        Collections.reverse(path);
        if (path.get(0) == start) return path;
        path.clear();
        return path;
    }

    // Perform a breadth first search on a graph a starting node 'start'.
    private void bfs(int start) {
        prev = new Integer[n];
        boolean[] visited = new boolean[n];
        Deque<Integer> queue = new ArrayDeque<>(n);

        // Start by visiting the 'start' node and add it to the queue.
        queue.offer(start);
        visited[start] = true;

        // Continue until the BFS is done.
        while (!queue.isEmpty()) {
            int node = queue.poll();
            List<Edge> edges = graph.get(node);

            // Loop through all edges attached to this node. Mark nodes as visited once they're
            // in the queue. This will prevent having duplicate nodes in the queue and speedup the BFS.
            for (Edge edge : edges) {
                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    prev[edge.to] = node;
                    queue.offer(edge.to);
                }
            }
        }
    }

    // Initialize an empty adjacency list that can hold up to n nodes.
    public static List<List<Edge>> createEmptyGraph(int n) {
        List<List<Edge>> graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        return graph;
    }

    // Add a directed edge from node 'u' to node 'v' with cost 'cost'.
    public static void addDirectedEdge(List<List<Edge>> graph, int u, int v, int cost) {
        graph.get(u).add(new Edge(u, v, cost));
    }

    // Add an undirected edge between nodes 'u' and 'v'.
    public static void addUndirectedEdge(List<List<Edge>> graph, int u, int v, int cost) {
        addDirectedEdge(graph, u, v, cost);
        addDirectedEdge(graph, v, u, cost);
    }

    // Add an undirected unweighted edge between nodes 'u' and 'v'. The edge added
    // will have a weight of 1 since its intended to be unweighted.
    public static void addUnweightedUndirectedEdge(List<List<Edge>> graph, int u, int v) {
        addUndirectedEdge(graph, u, v, 1);
    }

    private static String formatPath(List<Integer> path) {
        return path.stream().map(Object::toString).collect(Collectors.joining(" -> "));
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int size = in.nextInt();
        int edges = in.nextInt();
        int consultas = in.nextInt();
        int cont = 0;
        String origen, start;
        String destino, end;

        //inicializa el grafo
        List<List<Edge>> graph = createEmptyGraph(size);
        TreeMap<String, Integer> numeroCiudad = new TreeMap<>();
        TreeMap<Integer, String> nombreCiudad = new TreeMap<>();


        for (int i = 0; i < edges; i++) {
            origen = in.next();
            destino = in.next();

            if (!numeroCiudad.containsKey(origen)) {
                numeroCiudad.put(origen, cont);
                nombreCiudad.put(cont, origen);
                cont++;
            }

            if (!numeroCiudad.containsKey(destino)) {
                numeroCiudad.put(destino, cont);
                nombreCiudad.put(cont, destino);
                cont++;
            }
            addUnweightedUndirectedEdge(graph, numeroCiudad.get(origen), numeroCiudad.get(destino));
        }
        OnePiece solver;
        solver = new OnePiece(graph);

        for (int i = 0; i < consultas; i++) {
            start = in.next();
            end = in.next();
            List<Integer> path = solver.reconstructPath(numeroCiudad.get(start), numeroCiudad.get(end));

            MutableGraph r = mutGraph("onepiece").setDirected(true).use((gr, ctx) -> {
                // recorre todos los nodos
                for (int j = 0; j < graph.size(); j++) {
                    // recorre todas las conexiones de cada nodo
                    if (!path.contains(j)) {
                        //estilo del nodo que no está en el camino
                        mutNode(nombreCiudad.get(j)).add(Color.WHITE.fill());
                    } else {
                        //estilo del nodo que está en el camino
                        mutNode(nombreCiudad.get(j)).add(Color.BLUEVIOLET.fill());
                    }

                    for (int k = 0; k < graph.get(j).size(); k++) {
                        if ((path.contains(graph.get(j).get(k).from) && path.contains(graph.get(j).get(k).to)) && ((path.indexOf(graph.get(j).get(k).from) + 1) == path.indexOf(graph.get(j).get(k).to))) {
                            //Estilo de las aristas que estan en el camino
                            linkAttrs().add(Style.BOLD, Color.YELLOW);
                        } else {
                            //Estilo de las aristas que no estan en el camino
                            linkAttrs().add(Style.BOLD, Color.BLACK);
                        }
                        //crea arrista
                        mutNode(nombreCiudad.get(graph.get(j).get(k).from)).addLink(mutNode(nombreCiudad.get(graph.get(j).get(k).to)));
                    }
                }
            });

            r.graphAttrs().add(Rank.dir(TOP_TO_BOTTOM), Color.WHITE.gradient(Color.rgb("888888")).background().angle(90)).nodeAttrs().add().nodes().forEach(node -> node.add(Color.named(node.name().toString()), Style.lineWidth(2), Style.FILLED));
            //exportar grafo a imagen
            Graphviz.fromGraph(r).height(1080).width(2048).render(Format.PNG).toFile(new File("resultado/onepiece" + i + ".png"));

            System.out.printf("El camino mas corto de %s hasta %s es: [%s]\n", start, end, formatPath(path));
            for (Integer integer : path) {
                System.out.print(nombreCiudad.get(integer));
            }
            System.out.println();
        }
        in.close();
    }
}

