/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.utils;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import java.awt.Dimension;
import javax.swing.JFrame;
import telefunken.core.Edge;
import telefunken.core.Vertex;

/**
 *
 * @author Bilal Khan
 */
public class Visualization {
    
    public static void showGraph(DirectedSparseGraph<Vertex,Edge> g) 
    {
        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Vertex, Edge> layout = new FRLayout<Vertex, Edge>(g);
        layout.setSize(new Dimension(1500,1500)); // sets the initial size of the space
        BasicVisualizationServer<Vertex,Edge> vv =
        new BasicVisualizationServer<Vertex,Edge>(layout);
        vv.setPreferredSize(new Dimension(1500,1500)); //Sets the viewing area size

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true); 
    }
    
    private static final String OUTPUT_FILE = "graph.jpg";
}
