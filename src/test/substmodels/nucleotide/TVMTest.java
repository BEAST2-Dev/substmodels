package test.substmodels.nucleotide;


import beast.core.Description;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.TVM;

/**
 * Test TVM matrix exponentiation
 *
 */
@Description("Test TVM matrix exponentiation")
public class TVMTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([0.4, 0.3, 0.2, 0.1])
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
    protected UnequalBaseFrequencies test0 = new UnequalBaseFrequencies() {
        @Override
        public Double[] getPi() {
            return new Double[]{0.4, 0.3, 0.2, 0.1};
        }

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
                    0.926032879344,  0.002501473304,  0.069681075145,  0.001784572207,
                    0.003335297739,  0.957195914822,  0.00364943052 ,  0.035819356919,
                    0.13936215029 ,  0.005474145781,  0.838262134317,  0.016901569612,
                    0.00713828883 ,  0.107458070758,  0.033803139225,  0.851600501187
            };
        }
    };


    UnequalBaseFrequencies[] all = {test0};

    public void testTVM() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f); // "estimate", true

            TVM tvm = new TVM();
            RealParameter tvmRates = new RealParameter(test.getRates());
            tvm.initByName("rates", tvmRates, "frequencies", freqs);
            tvm.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tvm.getSubstitution(i) + " : " + tvm.getRate(i));

            assertEquals(false, tvm.getRateAC()== tvm.getRateAG());
            assertEquals(false, tvm.getRateAC()== tvm.getRateAT());
            assertEquals(false, tvm.getRateAC()== tvm.getRateCG());
            assertEquals(false, tvm.getRateAC()== tvm.getRateCT());
            assertEquals(false, tvm.getRateAC()== tvm.getRateGT());
            assertEquals(false, tvm.getRateAG()== tvm.getRateAT());
            assertEquals(false, tvm.getRateAG()== tvm.getRateCG());
            assertEquals(false, tvm.getRateAG()== tvm.getRateGT());
            assertEquals(false, tvm.getRateCG()== tvm.getRateGT());
            assertEquals(false, tvm.getRateCT()== tvm.getRateGT());
            // AG=CT
            assertEquals(true, tvm.getRateAG()== tvm.getRateCT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tvm.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}