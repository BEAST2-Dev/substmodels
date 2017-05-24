package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.TPM1uf;

@Description("Test TPM1uf matrix exponentiation")
public class TPM1ufTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([0.4, 0.3, 0.2, 0.1])
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
                    0.867261544828,  0.043790914387,  0.051602598024,  0.037344942761,
                    0.058387885849,  0.841290587716,  0.074689885522,  0.025631640912,
                    0.103205196048,  0.112034828283,  0.769484371808,  0.015275603861,
                    0.149379771044,  0.076894922736,  0.030551207723,  0.743174098497
            };
        }
    };


    UnequalBaseFrequencies[] all = {test0};

    public void testTPM1uf() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f); // "estimate", true

            TPM1uf tpm1uf = new TPM1uf();
            RealParameter rates = new RealParameter(test.getRates());
            tpm1uf.initByName("rates", rates, "frequencies", freqs);
            tpm1uf.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + tpm1uf.getSubstitution(i) + " : " + tpm1uf.getRate(i));

            // AC=GT
            assertEquals(true, tpm1uf.getRateAC()== tpm1uf.getRateGT() &&
                    tpm1uf.getRateAC()!=tpm1uf.getRateAT() && tpm1uf.getRateAC()!=tpm1uf.getRateAG());
            // AT=CG
            assertEquals(true, tpm1uf.getRateAT()== tpm1uf.getRateCG() &&
                    tpm1uf.getRateAT()!=tpm1uf.getRateAC() && tpm1uf.getRateAT()!=tpm1uf.getRateAG());
            // AG=CT
            assertEquals(true, tpm1uf.getRateAG()==tpm1uf.getRateCT() &&
                    tpm1uf.getRateAG()!=tpm1uf.getRateAC() && tpm1uf.getRateAG()!=tpm1uf.getRateAT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            tpm1uf.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}