package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.TPM2uf;

@Description("Test TPM2uf matrix exponentiation")
public class TPM2ufTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([0.4, 0.3, 0.2, 0.1])
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
            return 0.2;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.884425845071,  0.045894121233,  0.054381993286,  0.015298040411,
                    0.061192161643,  0.832485507245,  0.07816790575 ,  0.028154425361,
                    0.108763986572,  0.117251858625,  0.734900201928,  0.039083952875,
                    0.061192161643,  0.084463276084,  0.07816790575 ,  0.776176656523
            };
        }
    };


    UnequalBaseFrequencies[] all = {test0};

    public void testTPM2uf() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f); // "estimate", true

            TPM2uf tpm2uf = new TPM2uf();
            RealParameter rates = new RealParameter(test.getRates());
            tpm2uf.initByName("rates", rates, "frequencies", freqs);
            tpm2uf.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tpm2uf.getSubstitution(i) + " : " + tpm2uf.getRate(i));

            // AC=AT
            assertEquals(true, tpm2uf.getRateAC()== tpm2uf.getRateAT() &&
                    tpm2uf.getRateAC()!=tpm2uf.getRateCG() && tpm2uf.getRateAC()!=tpm2uf.getRateAG());
            // CG=GT
            assertEquals(true, tpm2uf.getRateCG()== tpm2uf.getRateGT() &&
                    tpm2uf.getRateCG()!=tpm2uf.getRateAC() && tpm2uf.getRateCG()!=tpm2uf.getRateAG());
            // AG=CT
            assertEquals(true, tpm2uf.getRateAG()==tpm2uf.getRateCT() &&
                    tpm2uf.getRateAG()!=tpm2uf.getRateAC() && tpm2uf.getRateAG()!=tpm2uf.getRateAT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tpm2uf.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}