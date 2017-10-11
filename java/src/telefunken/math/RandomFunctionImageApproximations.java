/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.math;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Bilal Khan
 */
public class RandomFunctionImageApproximations {
    
    public static double expectedImageSize(int hx, int nx) 
    {
        BigDecimal ans=BigDecimal.ZERO;
        for (int kx=0; kx<=hx;kx++) {
            BigInteger k = new BigInteger(""+kx);
            BigInteger hCk = Combinatorics.Choose(hx, kx);
            BigDecimal kovern = (new BigDecimal(""+kx)).divide(new BigDecimal(""+nx));
            BigDecimal kovern_pown = kovern.pow(nx);
            
            BigDecimal term = (new BigDecimal(k.multiply(hCk))).multiply(kovern_pown);
            ans = ans.add(term);
        }
        return ans.doubleValue();
    }
}
