package test.substmodels.nucleotide;


import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.SYM;

/**
 * Test SYM matrix exponentiation
 *
 */
@Description("Test SYM matrix exponentiation")
public class SYMTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([.25, .25, .25, .25])
     * d = 0.1
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 0.2, 10, .3], [0.2, 0, 0.4, 5], [10, 0.4, 0, 0.5], [0.3, 5, 0.5, 0]])
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
            return new Double[] {0.2, 10.0, 0.3, 0.4, 5.0, 0.5};
        }

        @Override
		public double getDistance() {
            return 0.1;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.886360401447,  0.002594129576,  0.107315348219,  0.003730120758,
                    0.002594129576,  0.93573730447 ,  0.004733198723,  0.056935367232,
                    0.107315348219,  0.004733198723,  0.882087475436,  0.005863977622,
                    0.003730120758,  0.056935367232,  0.005863977622,  0.933470534387
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testSYM() throws Exception {
        for (EqualBaseFrequencies test : all) {
            
            SYM sym = new SYM();
            RealParameter symRates = new RealParameter(test.getRates());
            sym.initByName("rates", symRates);
            sym.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + sym.getSubstitution(i) + " : " + sym.getRate(i));

            assertEquals(false, sym.getRateAC()== sym.getRateAG());
            assertEquals(false, sym.getRateAC()== sym.getRateAT());
            assertEquals(false, sym.getRateAC()== sym.getRateCG());
            assertEquals(false, sym.getRateAC()== sym.getRateCT());
            assertEquals(false, sym.getRateAC()== sym.getRateGT());
            assertEquals(false, sym.getRateAG()== sym.getRateAT());
            assertEquals(false, sym.getRateAG()== sym.getRateCG());
            assertEquals(false, sym.getRateAG()== sym.getRateCT());
            assertEquals(false, sym.getRateAG()== sym.getRateGT());
            assertEquals(false, sym.getRateCG()== sym.getRateGT());
            assertEquals(false, sym.getRateCT()== sym.getRateGT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            sym.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}