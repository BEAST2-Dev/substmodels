package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.TIM3uf;

@Description("Test TIM3uf matrix exponentiation")
public class TIM3ufTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([0.4, 0.3, 0.2, 0.1])
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
                    0.808287239489,  0.065896148199,  0.076235614109,  0.049580998203,
                    0.087861530931,  0.804818897665,  0.043930765466,  0.063388805938,
                    0.152471228218,  0.065896148199,  0.73205162538 ,  0.049580998203,
                    0.198323992812,  0.190166417815,  0.099161996406,  0.512347592967
            };
        }
    };


    UnequalBaseFrequencies[] all = {test0};

    public void testTIM3uf() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f); // "estimate", true

            TIM3uf tim3uf = new TIM3uf();
            RealParameter rates = new RealParameter(test.getRates());
            tim3uf.initByName("rates", rates, "frequencies", freqs);
            tim3uf.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tim3uf.getSubstitution(i) + " : " + tim3uf.getRate(i));

            // AC=CG
            assertEquals(true, tim3uf.getRateAC()== tim3uf.getRateCG() &&
                    tim3uf.getRateAC()!=tim3uf.getRateAT() && tim3uf.getRateAC()!=tim3uf.getRateAG());
            // AT=GT
            assertEquals(true, tim3uf.getRateAT()== tim3uf.getRateGT() &&
                    tim3uf.getRateAT()!=tim3uf.getRateCT() && tim3uf.getRateCG()!=tim3uf.getRateAG());
            // AG!=CT
            assertEquals(true, tim3uf.getRateAC()!=tim3uf.getRateAG() &&
                    tim3uf.getRateAC()!=tim3uf.getRateCT() && tim3uf.getRateAG()!=tim3uf.getRateCT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tim3uf.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}