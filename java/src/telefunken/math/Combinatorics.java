/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import telefunken.math.bigdecimalutils.BigDecimalMath;

/**
 *
 * @author Bilal Khan
 */
public class Combinatorics {
    
    public static BigInteger Factorial(int n) {
        if (n == 0) {
            return BigInteger.ONE;
        }
        BigInteger ans = BigInteger.ONE;
        for (int i = 1; i <= n; ++i) {
            BigInteger ix = new BigInteger(""+i);
            ans = ans.multiply(ix);
        }
        return ans;
    }

    public static BigInteger Power(int m, int p) {
        // m raised to the pth power
        BigInteger ans = BigInteger.ONE;
        BigInteger mx = new BigInteger(""+m);
        for (int i = 0; i < p; ++i) {
            ans = ans.multiply(mx);
        }
        return ans;
    }

    public static BigInteger Choose(int n, int k) {
        if (n < k) {
            return BigInteger.ZERO; // special
        }
        if (n == k) {
            return BigInteger.ONE; // short-circuit
        }
        int delta, iMax;

        if (k < n - k) // ex: Choose(100,3)
        {
            delta = n - k;
            iMax = k;
        } else // ex: Choose(100,97)
        {
            delta = k;
            iMax = n - k;
        }

        BigInteger ans = new BigInteger(""+(delta + 1));
        for (int i = 2; i <= iMax; ++i) {
            BigInteger dplusix = new BigInteger(""+(delta + i));
            BigInteger ix = new BigInteger(""+i);
            ans = ans.multiply(dplusix).divide(ix);
        }

        return ans;
    }

    public static BigInteger Stirling(int n, int k) {
        BigInteger sum = BigInteger.ZERO;

        for (int j = 0; j <= k; ++j) {
            BigInteger a = Power(-1, k - j);
            BigInteger b = Choose(k, j);
            BigInteger c = Power(j, n);
            BigInteger term = a.multiply(b).multiply(c);
//            System.out.println("a="+a);
//            System.out.println("b="+b);
//            System.out.println("c="+c);
//            System.out.println("term="+term);
            sum = sum.add(term);
        }

        BigInteger kfact = Factorial(k);
//        System.out.println("sum="+sum);
//        System.out.println("kfact="+kfact);
        return sum.divide(kfact);
    }
    
    private static int DIGITS_SMALL=100;
    private static int DIGITS_BIG=20000;
    
    public static BigInteger StirlingApprox(int n, int k) {
        BigDecimal dn=new BigDecimal(""+n);
        BigDecimal dk=new BigDecimal(""+k);
        
        BigDecimal dv = dn.divide(dk,DIGITS_SMALL,RoundingMode.HALF_UP);
        
        double G = -Lambert.branch0(-dv.doubleValue() * Math.exp(-dv.doubleValue()) );
        BigDecimal dG = new BigDecimal(""+G);
//        
//        System.out.println("n="+n);
//        System.out.println("k="+k);
//        System.out.println("G="+G);
        
        BigDecimal dn_minus_dk=dn.subtract(dk);
        BigDecimal sqrt_n_minus_k = BigDecimalMath.sqrt(dn_minus_dk);
        
        BigDecimal t1=BigDecimalMath.sqrt(dn.multiply(BigDecimal.ONE.subtract(dG)));
        BigDecimal t2=BigDecimalMath.pow(dG, dk);
        BigDecimal t3=BigDecimalMath.pow(dv.subtract(dG), dn_minus_dk);
        BigDecimal den = t1.multiply(t2).multiply(t3);
        BigDecimal lead_coeff = sqrt_n_minus_k.divide(den,DIGITS_BIG,RoundingMode.HALF_UP);
        System.out.println("sqrt_n_minus_k="+sqrt_n_minus_k);
        System.out.println("den="+den);
        System.out.println("coeff="+lead_coeff);
        BigDecimal base = dn_minus_dk.divide(BigDecimalMath.E,DIGITS_SMALL,RoundingMode.HALF_UP);
        BigDecimal term1 = BigDecimalMath.pow(base, dn_minus_dk);

        BigInteger nCk = Choose(n,k);
        System.out.println("base="+base);
        System.out.println("term="+term1);
        System.out.println("nCk="+nCk);
        
        BigDecimal dAnswer = lead_coeff.multiply(term1).multiply(new BigDecimal(nCk));
        BigInteger answer = dAnswer.toBigInteger();
        System.out.println("dAnswer="+dAnswer);
        System.out.println("answer="+answer);
        return answer;
    }
}
