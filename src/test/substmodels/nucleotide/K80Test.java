package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.K80;

/**
 * Test K80 matrix exponentiation
 */
@Description("Test HKY matrix exponentiation")
public class K80Test extends TestCase {

    public interface Instance {
        Double [] getRates();

        double getDistance();

        double[] getExpectedResult();
    }

    /*
     * Results obtained by running the following scilab code,
     *
     * k = 5 ; piQ = diag([.2, .3, .25, .25]) ; d = 0.1 ;
     * % Q matrix with zeroed diagonal
     * XQ = [0 1 k 1; 1 0 1 k; k 1 0 1; 1 k 1 0];
     *
     * xx = XQ * piQ ;
     *
     * % fill diagonal and normalize by total substitution rate
     * q0 = (xx + diag(-sum(xx,2))) / sum(piQ * sum(xx,2)) ;
     * expm(q0 * d)
     */
    protected Instance test0 = new Instance() {
        // public Double[] getPi() { return new Double[]{0.25, 0.25, 0.25, 0.25}; }

        @Override
        public Double [] getRates() {
            return new Double[] {0.5, 1.0};
        }

        @Override
		public double getDistance() {
            return 0.1;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.906563342722, 0.023790645491, 0.045855366296, 0.023790645491,
                    0.023790645491, 0.906563342722, 0.023790645491, 0.045855366296,
                    0.045855366296, 0.023790645491, 0.906563342722, 0.023790645491,
                    0.023790645491, 0.045855366296, 0.023790645491, 0.906563342722
            };
        }
    };


    Instance[] all = {test0};

    public void testK80() throws Exception {
        for (Instance test : all) {

            K80 k80 = new K80();
            RealParameter rates = new RealParameter(test.getRates());
            k80.initByName("rates", rates);

            double distance = test.getDistance();

            double[] mat = new double[4 * 4];
            k80.getTransitionProbabilities(null, distance, 0, 1, mat);
            final double[] result = test.getExpectedResult();

            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}