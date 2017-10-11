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
public class RealData_Telefunken {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // "C:\\Users\Bilal Khan\\Dropbox\Current Papers\\telefunken-java-shared\\telefunken-explorer"
        // "britekite"
        
        if (args.length != 2) {
            System.out.println("Arguments expected but not found: graphFile expName");
            System.out.println("Argcount = "+args.length);
            for (int i=0;i<args.length;i++) {
                System.out.println("Arg["+i+"] = "+args[i]);
            }
        }
        else {
            String graphFile = args[0];
            String expName = args[1];
            RealDataTest t = new RealDataTest();
            t.test1(graphFile, expName);
        }
    }
    
}
