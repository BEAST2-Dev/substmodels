package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.TPM2;

@Description("Test TPM2 matrix exponentiation")
public class TPM2Test extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([.25, .25, .25, .25])
     * d = 0.2
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 1, 2, 1], [1, 0, 3, 2], [2, 3, 0, 3], [1, 2, 3, 0]])
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
                    0.878138810072,  0.032097468293,  0.057666253341,  0.032097468293,
                    0.032097468293,  0.825297915841,  0.083235038389,  0.059369577476,
                    0.057666253341,  0.083235038389,  0.775863669881,  0.083235038389,
                    0.032097468293,  0.059369577476,  0.083235038389,  0.825297915841
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testTPM2() throws Exception {
        for (EqualBaseFrequencies test : all) {

            TPM2 tpm2 = new TPM2();
            RealParameter rates = new RealParameter(test.getRates());
            tpm2.initByName("rates", rates);
            tpm2.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tpm2.getSubstitution(i) + " : " + tpm2.getRate(i));

            // AC=AT
            assertEquals(true, tpm2.getRateAC()== tpm2.getRateAT() &&
                    tpm2.getRateAC()!=tpm2.getRateCG() && tpm2.getRateAC()!=tpm2.getRateAG());
            // CG=GT
            assertEquals(true, tpm2.getRateCG()== tpm2.getRateGT() &&
                    tpm2.getRateCG()!=tpm2.getRateAC() && tpm2.getRateCG()!=tpm2.getRateAG());
            // AG=CT
            assertEquals(true, tpm2.getRateAG()==tpm2.getRateCT() &&
                    tpm2.getRateAG()!=tpm2.getRateAC() && tpm2.getRateAG()!=tpm2.getRateAT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tpm2.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}