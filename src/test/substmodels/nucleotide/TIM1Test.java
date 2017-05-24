package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.TIM1;

@Description("Test TIM1 matrix exponentiation")
public class TIM1Test extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([.25, .25, .25, .25])
     * d = 0.3
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 1, 2, 3], [1, 0, 3, 4], [2, 3, 0, 1], [3, 4, 1, 0]])
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
                    0.783537640428,  0.044577861463,  0.071332157416,  0.100552340692,
                    0.044577861463,  0.7275631612  ,  0.100552340692,  0.127306636645,
                    0.071332157416,  0.100552340692,  0.783537640428,  0.044577861463,
                    0.100552340692,  0.127306636645,  0.044577861463,  0.7275631612
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testTIM1() throws Exception {
        for (EqualBaseFrequencies test : all) {

            TIM1 tim1 = new TIM1();
            RealParameter rates = new RealParameter(test.getRates());
            tim1.initByName("rates", rates);
            tim1.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tim1.getSubstitution(i) + " : " + tim1.getRate(i));

            // AC=GT
            assertEquals(true, tim1.getRateAC()== tim1.getRateGT() &&
                    tim1.getRateAC()!=tim1.getRateAT() && tim1.getRateAC()!=tim1.getRateAG());
            // AT=CG
            assertEquals(true, tim1.getRateAT()== tim1.getRateCG() &&
                    tim1.getRateAT()!=tim1.getRateAC() && tim1.getRateAT()!=tim1.getRateAG());
            // AG!=CT
            assertEquals(true, tim1.getRateAC()!=tim1.getRateAG() &&
                    tim1.getRateAC()!=tim1.getRateCT() && tim1.getRateAG()!=tim1.getRateCT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tim1.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}