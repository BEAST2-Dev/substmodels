package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.TPM3uf;

@Description("Test TPM3uf matrix exponentiation")
public class TPM3ufTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([0.4, 0.3, 0.2, 0.1])
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
                    0.851612986621,  0.04818900588 ,  0.05920944977 ,  0.040988557729,
                    0.06425200784 ,  0.875096208395,  0.03212600392 ,  0.028525779845,
                    0.11841889954 ,  0.04818900588 ,  0.792403536851,  0.040988557729,
                    0.163954230917,  0.085577339534,  0.081977115459,  0.66849131409
            };
        }
    };


    UnequalBaseFrequencies[] all = {test0};

    public void testTPM3uf() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f); // "estimate", true

            TPM3uf tpm3uf = new TPM3uf();
            RealParameter rates = new RealParameter(test.getRates());
            tpm3uf.initByName("rates", rates, "frequencies", freqs);
            tpm3uf.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tpm3uf.getSubstitution(i) + " : " + tpm3uf.getRate(i));

            // AC=CG
            assertEquals(true, tpm3uf.getRateAC()== tpm3uf.getRateCG() &&
                    tpm3uf.getRateAC()!=tpm3uf.getRateAT() && tpm3uf.getRateAC()!=tpm3uf.getRateAG());
            // AT=GT
            assertEquals(true, tpm3uf.getRateAT()== tpm3uf.getRateGT() &&
                    tpm3uf.getRateAT()!=tpm3uf.getRateAC() && tpm3uf.getRateAT()!=tpm3uf.getRateAG());
            // AG=CT
            assertEquals(true, tpm3uf.getRateAG()==tpm3uf.getRateCT() &&
                    tpm3uf.getRateAG()!=tpm3uf.getRateAC() && tpm3uf.getRateAG()!=tpm3uf.getRateAT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tpm3uf.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}