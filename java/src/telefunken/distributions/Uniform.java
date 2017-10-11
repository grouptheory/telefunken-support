/*
 * TELEFUNKEN POPULATION ESTIMATOR
 * Khan, Lee, Dombrowski, Fellows
 * @2017 All rights reserved
 */
package telefunken.distributions;

/**
 *
 * @author Bilal Khan
 */
public class Uniform extends AbstractDistribution {

    private int _degree;
    
    public Uniform(int n, int degree) {
        super(n);
        _degree = degree;
    }
    
    public int[] generate() 
    {
        int [] d = new int[_n];
        for (int i =0; i<_n; i++) {
            d[i]=_degree;
        }
        return d;
    }
    @Override
    public String getHumanReadableName() {
        return "Regular";
    }
}
