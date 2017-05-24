package test.substmodels.nucleotide;


import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.TVMef;

/**
 * Test TVMef matrix exponentiation
 *
 */
@Description("Test TVMef matrix exponentiation")
public class TVMefTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([.25, .25, .25, .25])
     * d = 0.1
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 0.2, 10, .3], [0.2, 0, 0.4, 10], [10, 0.4, 0, 5], [0.3, 10, 5, 0]])
     *
     * xx = XQ * piQ
     *
     * # fill diagonal and normalize by total substitution rate
     * q0 = (xx + np.diag(np.squeeze(np.asarray(-np.sum(xx, axis=1))))) / np.sum(piQ * np.sum(xx, axis=1))
     * expm(q0 * d)
     */
    protected EqualBaseFrequencies test0 = new EqualBaseFrequencies() {
        @Override
		public Double [] getRates() {
            return new Double[] {0.2, 10.0, 0.3, 0.4, 5.0};
        }

        @Override
		public double getDistance() {
            return 0.1;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.924841386585,  0.001651397824,  0.070005984376,  0.003501231215,
                    0.001651397824,  0.924131486211,  0.004198599114,  0.07001851685,
                    0.070005984376,  0.004198599114,  0.891234320911,  0.034561095599,
                    0.003501231215,  0.07001851685 ,  0.034561095599,  0.891919156335
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testTVMef() throws Exception {
        for (EqualBaseFrequencies test : all) {
            
            TVMef tvmef = new TVMef();
            RealParameter tvmefRates = new RealParameter(test.getRates());
            tvmef.initByName("rates", tvmefRates);
            tvmef.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tvmef.getSubstitution(i) + " : " + tvmef.getRate(i));

            assertEquals(false, tvmef.getRateAC()== tvmef.getRateAG());
            assertEquals(false, tvmef.getRateAC()== tvmef.getRateAT());
            assertEquals(false, tvmef.getRateAC()== tvmef.getRateCG());
            assertEquals(false, tvmef.getRateAC()== tvmef.getRateCT());
            assertEquals(false, tvmef.getRateAC()== tvmef.getRateGT());
            assertEquals(false, tvmef.getRateAG()== tvmef.getRateAT());
            assertEquals(false, tvmef.getRateAG()== tvmef.getRateCG());
            assertEquals(false, tvmef.getRateAG()== tvmef.getRateGT());
            assertEquals(false, tvmef.getRateCG()== tvmef.getRateGT());
            assertEquals(false, tvmef.getRateCT()== tvmef.getRateGT());
            // AG=CT
            assertEquals(true, tvmef.getRateAG()== tvmef.getRateCT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tvmef.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}