import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

class GraphDisplay extends JPanel {
    int[][] graph;
    int numVertices;
    java.util.List<Integer> shortestPath;
    int totalDistance;

    GraphDisplay(String filePath, java.util.List<Integer> shortestPath) throws FileNotFoundException {
        File input = new File(filePath);
        Scanner scanner = new Scanner(input);

        numVertices = scanner.nextInt();
        graph = new int[numVertices][numVertices];
        this.shortestPath = shortestPath;

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                graph[i][j] = scanner.nextInt();
            }
        }

        // Calculate the total distance
        totalDistance = 0;
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            totalDistance += graph[shortestPath.get(i)][shortestPath.get(i + 1)];
        }
    }

    private boolean isEdgeInShortestPath(int vertex1, int vertex2) {
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            if ((shortestPath.get(i) == vertex1 && shortestPath.get(i + 1) == vertex2)
                    || (shortestPath.get(i) == vertex2 && shortestPath.get(i + 1) == vertex1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int radius = 20;
        int padding = 50;

        int centerX = width / 2;
        int centerY = height / 2;

        double angle = 2 * Math.PI / numVertices;
        Point[] vertexPositions = new Point[numVertices];

        for (int i = 0; i < numVertices; i++) {
            int x = (int) (centerX + (Math.cos(i * angle) * (width / 2 - padding)));
            int y = (int) (centerY + (Math.sin(i * angle) * (height / 2 - padding)));
            vertexPositions[i] = new Point(x, y);
        }

        // Draw edges and weights
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (graph[i][j] != 0) {
                    if (isEdgeInShortestPath(i, j)) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.BLACK);
                    }

                    g.drawLine(vertexPositions[i].x, vertexPositions[i].y, vertexPositions[j].x, vertexPositions[j].y);

                    // Draw weights
                    int weightX = (vertexPositions[i].x + vertexPositions[j].x) / 2;
                    int weightY = (vertexPositions[i].y + vertexPositions[j].y) / 2;
                    String weightStr = Integer.toString(graph[i][j]);

                    // Create a background for the weight label
                    g.setColor(new Color(255, 255, 255, 200));
                    g.fillRect(weightX - 2, weightY - g.getFontMetrics().getAscent() + 3, g.getFontMetrics().stringWidth(weightStr) + 4, g.getFontMetrics().getHeight() - 6);

                    // Draw the weight string
                    g.setColor(Color.BLACK);
                    g.drawString(weightStr, weightX, weightY);
                }
            }
        }

        g.setColor(Color.ORANGE);
        for (int i = 0; i < numVertices; i++) {
            g.fillOval(vertexPositions[i].x - radius / 2, vertexPositions[i].y - radius / 2, radius, radius);
            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(i), vertexPositions[i].x - 3, vertexPositions[i].y + 3);
            g.setColor(Color.ORANGE);
        }

        // Draw the label with the total distance
        g.setColor(Color.BLACK);
        String totalDistanceLabel = "Total cost/distance: " + totalDistance;
        g.drawString(totalDistanceLabel, 10, getHeight() - 20);
    }


}
