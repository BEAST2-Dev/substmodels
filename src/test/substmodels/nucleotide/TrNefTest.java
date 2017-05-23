package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.TrNef;

@Description("Test TrNe matrix exponentiation")
public class TrNefTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([.25, .25, .25, .25])
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
    protected EqualBaseFrequencies test0 = new EqualBaseFrequencies() {
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
                    0.916323466704,  0.021263192817,  0.041150147661,  0.021263192817,
                    0.021263192817,  0.897301022863,  0.021263192817,  0.060172591502,
                    0.041150147661,  0.021263192817,  0.916323466704,  0.021263192817,
                    0.021263192817,  0.060172591502,  0.021263192817,  0.897301022863
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testTrNe() throws Exception {
        for (EqualBaseFrequencies test : all) {

            TrNef trNe = new TrNef();
            RealParameter rates = new RealParameter(test.getRates());
            trNe.initByName("rates", rates);
            trNe.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + trNe.getSubstitution(i) + " : " + trNe.getRate(i));

            // AC=AT=CG=GT
            assertEquals(true, trNe.getRateAC()==trNe.getRateAT() &&
                    trNe.getRateAC()==trNe.getRateCG() && trNe.getRateAC()== trNe.getRateGT());
            // AG!=CT
            assertEquals(true, trNe.getRateAC()!=trNe.getRateAG() &&
                    trNe.getRateAC()!=trNe.getRateCT() && trNe.getRateAG()!=trNe.getRateCT());

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            trNe.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();
            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}