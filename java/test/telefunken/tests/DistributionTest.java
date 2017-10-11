/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.tests;

import java.util.Arrays;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import telefunken.distributions.Exponential;
import telefunken.distributions.IDistributionGenerator;
import telefunken.distributions.LogNormal;
import telefunken.distributions.Poisson;
import telefunken.distributions.Uniform;

/**
 *
 * @author Bilal Khan
 */
public class DistributionTest {
    
    @Test
    public void test1() {
        
        dotest(30, true, false);
        dotest(10000, false, true);
    }
    
    void dotest(int CT, boolean print, boolean check) {
        if (print) System.out.println("=============================UNIFORM");
        int param1=5;
        IDistributionGenerator dg1 = new Uniform(CT,param1);
        int [] deg1 = dg1.generate();
        double sum1=0.0;
        for (int i=0;i<CT;i++) {
            if (print) System.out.print(""+deg1[i]+",");
            sum1+=deg1[i];
            assertEquals(deg1[i],5);
        }
        if (print) System.out.println("");
        double ave1=sum1/CT;
        
        if (print) System.out.println("=============================POISSON");
        double param2=5.0;
        IDistributionGenerator dg2 = new Poisson(CT,param2);
        int [] deg2 = dg2.generate();
        Arrays.sort(deg2);
        double sum2=0.0;
        for (int i=0;i<CT;i++) {
            sum2+=deg2[i];
            if (print) System.out.print(""+deg2[i]+",");
        }
        if (print) System.out.println("");
        double ave2=sum2/CT;
        
        if (print) System.out.println("=============================EXPONENTIAL");
        double param3=1.0/5.0;
        IDistributionGenerator dg3 = new Exponential(CT,param3);
        int [] deg3 = dg3.generate();
        Arrays.sort(deg3);
        double sum3=0.0;
        for (int i=0;i<CT;i++) {
            sum3+=deg3[i];
            if (print) System.out.print(""+deg3[i]+",");
        }
        if (print) System.out.println("");
        double ave3=sum3/CT;
        
        if (print) System.out.println("=============================LOGNORMAL");
        double param4=5.0;
        IDistributionGenerator dg4 = new LogNormal(CT,param4,1.0);
        int [] deg4 = dg4.generate();
        Arrays.sort(deg4);
        double sum4=0.0;
        for (int i=0;i<CT;i++) {
            sum4+=deg4[i];
            if (print) System.out.print(""+deg4[i]+",");
        }
        if (print) System.out.println("");
        double ave4=sum4/CT;
        
        double EPSI=0.01;
        if (check) {
            System.out.println("Checking mean of UNIFORM "+ave1);
            assertTrue(ave1 == param1);
            System.out.println("Checking mean of POISSON "+ave2);
            assertTrue(ave2-1.0 >= (1.0-EPSI)*param2       && ave2-1.0 <= (1.0+EPSI)*param2);
            System.out.println("Checking mean of EXPONENTIAL "+ave3);
            assertTrue(ave3-1.0 >= (1.0-EPSI)*(1.0/param3) && ave3-1.0 <= (1.0+EPSI)*(1.0/param3));
            System.out.println("Checking mean of LOGNORMAL "+ave4);
            assertTrue(ave4-1.0 >= (1.0-EPSI)*param4       && ave4-1.0 <= (1.0+EPSI)*param4);
        }
    }
}
