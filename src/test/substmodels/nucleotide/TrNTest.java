package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.TrN;

@Description("Test TrN matrix exponentiation")
public class TrNTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([0.4, 0.3, 0.2, 0.1])
     * d = 0.1
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 1, 2, 1], [1, 0, 1, 3], [2, 1, 0, 1], [1, 3, 1, 0]])
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
            return new Double[] {1.0, 2.0, 3.0};
        }

        @Override
		public double getDistance() {
            return 0.1;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.923919990609,  0.029102191778,  0.03727708702 ,  0.009700730593,
                    0.038802922371,  0.914398587505,  0.019401461186,  0.027397028938,
                    0.07455417404 ,  0.029102191778,  0.886642903588,  0.009700730593,
                    0.038802922371,  0.082191086815,  0.019401461186,  0.859604529629
            };
        }
    };


    UnequalBaseFrequencies[] all = {test0};

    public void testTrNe() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f); // "estimate", true

            TrN trN = new TrN();
            RealParameter rates = new RealParameter(test.getRates());
            trN.initByName("rates", rates, "frequencies", freqs);
            trN.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + trN.getSubstitution(i) + " : " + trN.getRate(i));

            // AC=AT=CG=GT
            assertEquals(true, trN.getRateAC()==trN.getRateAT() &&
                    trN.getRateAC()==trN.getRateCG() && trN.getRateAC()== trN.getRateGT());
            // AG!=CT
            assertEquals(true, trN.getRateAC()!=trN.getRateAG() &&
                    trN.getRateAC()!=trN.getRateCT() && trN.getRateAG()!=trN.getRateCT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            trN.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}