package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.TIM2uf;

@Description("Test TIM2uf matrix exponentiation")
public class TIM2ufTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([0.4, 0.3, 0.2, 0.1])
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
    protected UnequalBaseFrequencies test0 = new UnequalBaseFrequencies() {
        @Override
        public Double[] getPi() {
            return new Double[]{0.4, 0.3, 0.2, 0.1};
        }
        
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
                    0.848004762603,  0.061442861874,  0.070071421565,  0.020480953958,
                    0.081923815832,  0.755054682896,  0.099180935215,  0.063840566057,
                    0.140142843131,  0.148771402822,  0.661495286439,  0.049590467607,
                    0.081923815832,  0.19152169817 ,  0.099180935215,  0.627373550783
            };
        }
    };


    UnequalBaseFrequencies[] all = {test0};

    public void testTIM2uf() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f); // "estimate", true

            TIM2uf tim2uf = new TIM2uf();
            RealParameter rates = new RealParameter(test.getRates());
            tim2uf.initByName("rates", rates, "frequencies", freqs);
            tim2uf.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tim2uf.getSubstitution(i) + " : " + tim2uf.getRate(i));

            // AC=AT
            assertEquals(true, tim2uf.getRateAC()== tim2uf.getRateAT() &&
                    tim2uf.getRateAC()!=tim2uf.getRateCG() && tim2uf.getRateAC()!=tim2uf.getRateAG());
            // CG=GT
            assertEquals(true, tim2uf.getRateCG()== tim2uf.getRateGT() &&
                    tim2uf.getRateCG()!=tim2uf.getRateCT() && tim2uf.getRateCG()!=tim2uf.getRateAG());
            // AG!=CT
            assertEquals(true, tim2uf.getRateAC()!=tim2uf.getRateAG() &&
                    tim2uf.getRateAC()!=tim2uf.getRateCT() && tim2uf.getRateAG()!=tim2uf.getRateCT());


            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tim2uf.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}