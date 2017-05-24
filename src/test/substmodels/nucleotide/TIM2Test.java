package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.TIM2;

@Description("Test TIM2 matrix exponentiation")
public class TIM2Test extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([.25, .25, .25, .25])
     * d = 0.3
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 1, 2, 1], [1, 0, 3, 4], [2, 3, 0, 3], [1, 4, 3, 0]])
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
                    0.847204213099,  0.040767943097,  0.071259900708,  0.040767943097,
                    0.040767943097,  0.727703865901,  0.101751858319,  0.129776332684,
                    0.071259900708,  0.101751858319,  0.725236382655,  0.101751858319,
                    0.040767943097,  0.129776332684,  0.101751858319,  0.727703865901
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testTIM2() throws Exception {
        for (EqualBaseFrequencies test : all) {

            TIM2 tim2 = new TIM2();
            RealParameter rates = new RealParameter(test.getRates());
            tim2.initByName("rates", rates);
            tim2.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tim2.getSubstitution(i) + " : " + tim2.getRate(i));

            // AC=AT
            assertEquals(true, tim2.getRateAC()== tim2.getRateAT() &&
                    tim2.getRateAC()!=tim2.getRateCG() && tim2.getRateAC()!=tim2.getRateAG());
            // CG=GT
            assertEquals(true, tim2.getRateCG()== tim2.getRateGT() &&
                    tim2.getRateCG()!=tim2.getRateCT() && tim2.getRateCG()!=tim2.getRateAG());
            // AG!=CT
            assertEquals(true, tim2.getRateAC()!=tim2.getRateAG() &&
                    tim2.getRateAC()!=tim2.getRateCT() && tim2.getRateAG()!=tim2.getRateCT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tim2.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}