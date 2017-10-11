/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.math;

/**
 *
 * @author Bilal Khan
 */
public class Stats {
    
    public static class Moments 
    {
        private Moments() {}
        
        public double mean;
        public double std;
        public double na;
    }
    
    public static Moments process(Double[] data) {
        Moments m = new Moments();
        m.mean=mean(data);
        m.std=std(data,m.mean);
        m.na=na(data);
        return m;
    }
    
    private static double mean(Double[] data) {
        double sum=0.0;
        double count=0.0;
        for (int i=0;i<data.length; i++) {
            if ( ! Double.isNaN(data[i])) {
                sum+=data[i];
                count+=1.0;
            }
        }
        return sum/count;
    }
    
    private static double std(Double[] data, Double mean) {
        double sum=0.0;
        double count=0.0;
        for (int i=0;i<data.length; i++) {
            if ( ! Double.isNaN(data[i])) {
                sum+=(data[i]-mean)*(data[i]-mean);
                count+=1.0;
            }
        }
        return Math.sqrt(sum/count);
    }
    
    private static double na(Double[] data) {
        double count=0.0;
        for (int i=0;i<data.length; i++) {
            if (Double.isNaN(data[i])) {
                count+=1.0;
            }
        }
        return count/(double)data.length;
    }
}
