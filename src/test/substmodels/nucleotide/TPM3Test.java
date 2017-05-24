package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.TPM3;

@Description("Test TPM3 matrix exponentiation")
public class TPM3Test extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([.25, .25, .25, .25])
     * d = 0.2
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 1, 2, 3], [1, 0, 1, 2], [2, 1, 0, 3], [3, 2, 3, 0]])
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
                    0.825297915841,  0.032097468293,  0.059369577476,  0.083235038389,
                    0.032097468293,  0.878138810072,  0.032097468293,  0.057666253341,
                    0.059369577476,  0.032097468293,  0.825297915841,  0.083235038389,
                    0.083235038389,  0.057666253341,  0.083235038389,  0.775863669881
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testTPM3() throws Exception {
        for (EqualBaseFrequencies test : all) {

            TPM3 tpm3 = new TPM3();
            RealParameter rates = new RealParameter(test.getRates());
            tpm3.initByName("rates", rates);
            tpm3.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tpm3.getSubstitution(i) + " : " + tpm3.getRate(i));

            // AC=CG
            assertEquals(true, tpm3.getRateAC()== tpm3.getRateCG() &&
                    tpm3.getRateAC()!=tpm3.getRateAT() && tpm3.getRateAC()!=tpm3.getRateAG());
            // AT=GT
            assertEquals(true, tpm3.getRateAT()== tpm3.getRateGT() &&
                    tpm3.getRateAT()!=tpm3.getRateAC() && tpm3.getRateAT()!=tpm3.getRateAG());
            // AG=CT
            assertEquals(true, tpm3.getRateAG()==tpm3.getRateCT() &&
                    tpm3.getRateAG()!=tpm3.getRateAC() && tpm3.getRateAG()!=tpm3.getRateAT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tpm3.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}