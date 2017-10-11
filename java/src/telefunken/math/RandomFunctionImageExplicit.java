/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.math;

import java.util.ArrayList;

/**
 *
 * @author Bilal Khan
 */
public class RandomFunctionImageExplicit {
    
    private static class Node {
        double imagesize;
        double branchProb;
        double cumulativeProb;
    }
    
    public static double expectedImageSize(int h, int n) {
        double ans=0.0;
        ArrayList<Node> partial = new ArrayList<Node>();
        Node v0=new Node();
        v0.branchProb=1.0;
        v0.cumulativeProb=1.0;
        v0.imagesize=1;
        partial.add(v0);
        
        ArrayList<Node> completed = expectedImageSize(partial, h, n-1);
        
        for (Node v : completed) {
            ans += v.cumulativeProb * v.imagesize;
        }
        return ans;
    }
    
    public static ArrayList<Node> expectedImageSize(ArrayList<Node> partial, double h, double n) {
        ArrayList<Node> completed = new ArrayList<Node>();
        for (Node v : partial) {
            Node v1=new Node();
            v1.branchProb=(h-v.imagesize)/h;
            v1.cumulativeProb=v.cumulativeProb*v1.branchProb;
            v1.imagesize=v.imagesize+1.0;
            completed.add(v1);
            
            Node v2=new Node();
            v2.branchProb=(v.imagesize)/h;
            v2.cumulativeProb=v.cumulativeProb*v2.branchProb;
            v2.imagesize=v.imagesize;
            completed.add(v2);
        }
        
        if (n==1) {
            return completed;
        }
        else {
            partial.clear();
            return expectedImageSize(completed, h, n-1);
        }
    }
}
