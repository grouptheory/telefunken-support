/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken;

import telefunken.experimenters.ResultsDatabase;

/**
 *
 * @author Bilal Khan
 */
public class Telefunken {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Arguments expected but not found: graph-family file-prefix");
            System.out.println("Argcount = "+args.length);
            for (int i=0;i<args.length;i++) {
                System.out.println("Arg["+i+"] = "+args[i]);
            }
        }
        else {
            int graphFamily = Integer.parseInt(args[0]);
            EstimationTest t = new EstimationTest();
            t.test1(graphFamily, args[1]);
        }
    }
    
}
