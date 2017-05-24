package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.TIM3;

@Description("Test TIM3 matrix exponentiation")
public class TIM3Test extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([.25, .25, .25, .25])
     * d = 0.3
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 1, 2, 3], [1, 0, 1, 4], [2, 1, 0, 3], [3, 4, 3, 0]])
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
            return new Double[] {1.0, 2.0, 3.0, 4.0};
        }

        @Override
		public double getDistance() {
            return 0.3;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.78353838664 ,  0.043309922451,  0.073798790951,  0.099352899958,
                    0.043309922451,  0.786005766388,  0.043309922451,  0.127374388711,
                    0.073798790951,  0.043309922451,  0.78353838664 ,  0.099352899958,
                    0.099352899958,  0.127374388711,  0.099352899958,  0.673919811374
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testTIM3() throws Exception {
        for (EqualBaseFrequencies test : all) {

            TIM3 tim3 = new TIM3();
            RealParameter rates = new RealParameter(test.getRates());
            tim3.initByName("rates", rates);
            tim3.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tim3.getSubstitution(i) + " : " + tim3.getRate(i));

            // AC=CG
            assertEquals(true, tim3.getRateAC()== tim3.getRateCG() &&
                    tim3.getRateAC()!=tim3.getRateAT() && tim3.getRateAC()!=tim3.getRateAG());
            // AT=GT
            assertEquals(true, tim3.getRateAT()== tim3.getRateGT() &&
                    tim3.getRateAT()!=tim3.getRateCT() && tim3.getRateCG()!=tim3.getRateAG());
            // AG!=CT
            assertEquals(true, tim3.getRateAC()!=tim3.getRateAG() &&
                    tim3.getRateAC()!=tim3.getRateCT() && tim3.getRateAG()!=tim3.getRateCT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tim3.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}