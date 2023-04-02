//Camila Aichele
//6246396
//Section: 

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

class ShortestPath {

    static int numVertices;
    static int[][] graph;

    ShortestPath(int v) {
        numVertices = v;
        graph = new int[v][v];
    }

    // find a vertex with minimum distance
    int minDistance(int[] distance, Boolean[] visited) {
        // Initialize min value
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i] && distance[i] <= min) {
                min = distance[i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    // print the array of distances (distance)
    void printMinPath(int[] distance, int start, int end, List<Integer> path) {
        System.out.println("Graph:");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                System.out.print(graph[i][j] + " ");
            }
            System.out.println();
        }

        int pathDistance = distance[end];
        System.out.println("\nShortest path from " + start + " to " + end + ":");
        System.out.println("Vertex# \t Distance \t Visited");
        for (int i = 0; i < numVertices; i++) {
            System.out.print(i + " \t\t\t ");
            if (distance[i] == Integer.MAX_VALUE) {
                System.out.print("-1 \t\t\t");
            } else {
                System.out.print(distance[i] + " \t\t\t");
            }
            if (path.contains(i)) {
                System.out.println("yes");
            } else {
                System.out.println("no");
            }
        }
        System.out.println("\nDistance from " + start + " to " + end + ": " + pathDistance);
    }

    // Implementation of Dijkstra's algorithm for graph (adjacency matrix)
    List<Integer> algoDijkstra(int start, int end) {
        int[] distance = new int[numVertices];
        int[] previous = new int[numVertices];
        Boolean[] visited = new Boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            distance[i] = Integer.MAX_VALUE;
            previous[i] = -1;
            visited[i] = false;
        }

        distance[start] = 0;

        for (int i = 0; i < numVertices - 1; i++) {
            int u = minDistance(distance, visited);
            visited[u] = true;

            if (u == end) {
                break;
            }

            for (int j = 0; j < numVertices; j++) {
                if (!visited[j] && graph[u][j] != 0 && distance[u] != Integer.MAX_VALUE
                        && distance[u] + graph[u][j] < distance[j]) {
                    distance[j] = distance[u] + graph[u][j];
                    previous[j] = u;
                }
            }
        }

        List<Integer> path = getShortestPath(distance, previous, start, end);
        printMinPath(distance, start, end, path);
        return path;
    }

    List<Integer> getShortestPath(int[] distance, int[] previous, int start, int end) {
        List<Integer> path = new ArrayList<>();

        if (distance[end] == Integer.MAX_VALUE) {
            return path; // Empty path if there's no connection between the vertices
        }

        for (int vertex = end; vertex != -1; vertex = previous[vertex]) {
            path.add(vertex);
        }

        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        try {
            File input = new File("/Users/camilaaichele/Desktop/Project/src/graph.txt");
            Scanner scanner = new Scanner(input);

            numVertices = scanner.nextInt();
            ShortestPath graph = new ShortestPath(numVertices);

            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    graph.graph[i][j] = scanner.nextInt();
                }
            }

            int s = 0;
            int t = 0;
            while (scanner.hasNextInt()) {
                s = t;
                t = scanner.nextInt();
            }

            List<Integer> shortestPath = graph.algoDijkstra(s, t);

            //graphical output
            try {
                GraphDisplay graphDisplay = new GraphDisplay("/Users/camilaaichele/Desktop/Project/src/graph.txt", shortestPath);
                JFrame frame = new JFrame("Graph Display");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(graphDisplay);
                frame.setSize(400, 400);
                frame.setVisible(true);
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + e.getMessage());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}