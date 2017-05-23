package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.JC;

/**
 * Test JC matrix exponentiation
 */
@Description("Test JC matrix exponentiation")
public class JCTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     * k = 1
     * piQ = np.diag([.25, .25, .25, .25])
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
    protected EqualBaseFrequencies test0 = new EqualBaseFrequencies() {
        // public Double[] getPi() { return new Double[]{0.25, 0.25, 0.25, 0.25}; }

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
                    0.906379989282,  0.031206670239,  0.031206670239,  0.031206670239,
                    0.031206670239,  0.906379989282,  0.031206670239,  0.031206670239,
                    0.031206670239,  0.031206670239,  0.906379989282,  0.031206670239,
                    0.031206670239,  0.031206670239,  0.031206670239,  0.906379989282
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testJC() throws Exception {
        for (EqualBaseFrequencies test : all) {

            JC jc = new JC();
            RealParameter rates = new RealParameter(test.getRates());
            jc.initByName("rates", rates);

            double distance = test.getDistance();

            double[] mat = new double[4 * 4];
            jc.getTransitionProbabilities(null, distance, 0, 1, mat);
            final double[] result = test.getExpectedResult();

            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}