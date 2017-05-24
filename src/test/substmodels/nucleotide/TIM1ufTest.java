package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.TIM1uf;

@Description("Test TIM1uf matrix exponentiation")
public class TIM1ufTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([0.4, 0.3, 0.2, 0.1])
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
                    0.825871651293,  0.060874601338,  0.067043906735,  0.046209840634,
                    0.081166135118,  0.765617592587,  0.094589810557,  0.058626461738,
                    0.134087813471,  0.141884715835,  0.701565807625,  0.022461663069,
                    0.184839362534,  0.175879385215,  0.044923326138,  0.594357926113
            };
        }
    };


    UnequalBaseFrequencies[] all = {test0};

    public void testTIM1uf() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f); // "estimate", true

            TIM1uf tim1uf = new TIM1uf();
            RealParameter rates = new RealParameter(test.getRates());
            tim1uf.initByName("rates", rates, "frequencies", freqs);
            tim1uf.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tim1uf.getSubstitution(i) + " : " + tim1uf.getRate(i));

            // AC=GT
            assertEquals(true, tim1uf.getRateAC()== tim1uf.getRateGT() &&
                    tim1uf.getRateAC()!=tim1uf.getRateAT() && tim1uf.getRateAC()!=tim1uf.getRateAG());
            // AT=CG
            assertEquals(true, tim1uf.getRateAT()== tim1uf.getRateCG() &&
                    tim1uf.getRateAT()!=tim1uf.getRateAC() && tim1uf.getRateAT()!=tim1uf.getRateAG());
            // AG!=CT
            assertEquals(true, tim1uf.getRateAC()!=tim1uf.getRateAG() &&
                    tim1uf.getRateAC()!=tim1uf.getRateCT() && tim1uf.getRateAG()!=tim1uf.getRateCT());


            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tim1uf.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}