/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 *
 * @author Bilal Khan
 */
public class RandomFunctionImageAnalytic {
    
    private static int DIGITS=100;
    
    public static double expectedImageSize(int hx, int nx) 
    {
        BigDecimal ans=BigDecimal.ZERO;
        for (int kx=0; kx<=hx;kx++) {
            BigInteger k = new BigInteger(""+kx);
            BigInteger kfact = Combinatorics.Factorial(kx);
            BigInteger s2nk = Combinatorics.Stirling(nx, kx);
            BigInteger hCk = Combinatorics.Choose(hx, kx);
            BigInteger hpown = Combinatorics.Power(hx, nx);
            BigDecimal numer = new BigDecimal(k.multiply(kfact).multiply(s2nk).multiply(hCk));
            BigDecimal denom = new BigDecimal(hpown);
            ans = ans.add(numer.divide(denom, DIGITS, RoundingMode.HALF_UP));
        }
        return ans.doubleValue();
    }
}
