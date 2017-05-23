package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.TPM1;

@Description("Test TPM1 matrix exponentiation")
public class TPM1Test extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([.25, .25, .25, .25])
     * d = 0.2
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 1, 2, 3], [1, 0, 3, 2], [2, 3, 0, 1], [3, 2, 1, 0]])
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
            return new Double[] {1.0, 2.0, 3.0};
        }

        @Override
		public double getDistance() {
            return 0.2;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.825297600504,  0.032968054783,  0.057666568678,  0.084067776035,
                    0.032968054783,  0.825297600504,  0.084067776035,  0.057666568678,
                    0.057666568678,  0.084067776035,  0.825297600504,  0.032968054783,
                    0.084067776035,  0.057666568678,  0.032968054783,  0.825297600504
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testTPM1() throws Exception {
        for (EqualBaseFrequencies test : all) {

            TPM1 tpm1 = new TPM1();
            RealParameter rates = new RealParameter(test.getRates());
            tpm1.initByName("rates", rates);
            tpm1.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tpm1.getSubstitution(i) + " : " + tpm1.getRate(i));

            // AC=GT
            assertEquals(true, tpm1.getRateAC()== tpm1.getRateGT() &&
                    tpm1.getRateAC()!=tpm1.getRateAT() && tpm1.getRateAC()!=tpm1.getRateAG());
            // AT=CG
            assertEquals(true, tpm1.getRateAT()== tpm1.getRateCG() &&
                    tpm1.getRateAT()!=tpm1.getRateAC() && tpm1.getRateAT()!=tpm1.getRateAG());
            // AG=CT
            assertEquals(true, tpm1.getRateAG()==tpm1.getRateCT() &&
                    tpm1.getRateAG()!=tpm1.getRateAC() && tpm1.getRateAG()!=tpm1.getRateAT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tpm1.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}