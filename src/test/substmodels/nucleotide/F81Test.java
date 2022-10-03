package test.substmodels.nucleotide;

import beast.base.core.Description;
import beast.base.inference.parameter.RealParameter;
import beast.base.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.F81;

/**
 * Test F81 matrix exponentiation
 */
@Description("Test F81 matrix exponentiation")
public class F81Test extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     * k = 1
     * piQ = np.diag([0.4, 0.3, 0.2, 0.1])
     * d = 0.1
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 1, k, 1], [1, 0, 1, k], [k, 1, 0, 1], [1, k, 1, 0]])
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
            return new Double[] {1.0};
        }

        @Override
		public double getDistance() {
            return 0.1;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.92012673985 ,  0.039936630075,  0.02662442005 ,  0.013312210025,
                    0.0532488401  ,  0.906814529825,  0.02662442005 ,  0.013312210025,
                    0.0532488401  ,  0.039936630075,  0.8935023198  ,  0.013312210025,
                    0.0532488401  ,  0.039936630075,  0.02662442005 ,  0.880190109775
            };
        }
    };


    UnequalBaseFrequencies[] all = {test0};

    public void testF81() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f);

            F81 f81 = new F81();
            RealParameter rates = new RealParameter(test.getRates());
            f81.initByName("rates", rates, "frequencies", freqs);
            
            // AC=AT=AG=CG=CT=GT
            assertEquals(true, f81.getRateAC()==f81.getRateAT() &&
                    f81.getRateAC()==f81.getRateCG() && f81.getRateAC()== f81.getRateGT() &&
                    f81.getRateAC()==f81.getRateAG() && f81.getRateAC()==f81.getRateCT());
            
            double distance = test.getDistance();

            double[] mat = new double[4 * 4];
            f81.getTransitionProbabilities(null, distance, 0, 1, mat);
            final double[] result = test.getExpectedResult();

            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}