/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.experimenters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author Bilal Khan
 */
public class ResultsFile {
    
    private FileWriter fw;
    private BufferedWriter bw;
    private PrintWriter out;
    
    public ResultsFile(String filename) {
        try {
            fw = new FileWriter(filename, false);
            bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void write(String s) {
        out.println(s);
    }
    
    public void close() {
        try {
            out.flush();
            bw.flush();
            fw.flush();
            
            if( out != null ){
               out.close(); // Will close bw and fw too
            }
            else if( bw != null ){
               bw.close(); // Will close fw too
            }
            else if( fw != null ){
               fw.close();
            }
            else{
               // Oh boy did it fail hard! :3
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
