package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.HKY;

/**
 * Test HKY matrix exponentiation
 *
 * @author Joseph Heled
 *         Date: 7/11/2007
 *         imported by Walter Xie from BEAST 1
 */
@Description("Test HKY matrix exponentiation")
public class HKYTest extends TestCase {

    /*
     * Results obtained by running the following scilab code,
     *
     * k = 5 ; piQ = diag([.2, .3, .25, .25]) ; d = 0.1 ;
     * % Q matrix with zeroed diagonal
     * XQ = [0 1 k 1; 1 0 1 k; k 1 0 1; 1 k 1 0];
     *
     * xx = XQ * piQ ;
     *
     * % fill diagonal and normalize by total substitution rate
     * q0 = (xx + diag(-sum(xx,2))) / sum(piQ * sum(xx,2)) ;
     * expm(q0 * d)
     */
    protected UnequalBaseFrequencies test1 = new UnequalBaseFrequencies() {
        @Override
		public Double[] getPi() {
            return new Double[]{0.50, 0.20, 0.2, 0.1};
        }

        @Override
        public Double [] getRates() {
            return new Double[] {0.5, 1.0};
        }

        @Override
		public double getDistance() {
            return 0.1;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.928287993055, 0.021032136637, 0.040163801989, 0.010516068319,
                    0.052580341593, 0.906092679369, 0.021032136637, 0.020294842401,
                    0.100409504972, 0.021032136637, 0.868042290072, 0.010516068319,
                    0.052580341593, 0.040589684802, 0.021032136637, 0.885797836968
            };
        }
    };

    protected UnequalBaseFrequencies test2 = new UnequalBaseFrequencies() {
        @Override
		public Double[] getPi() {
            return new Double[]{0.20, 0.30, 0.25, 0.25};
        }

        @Override
        public Double [] getRates() {
            return new Double[] {0.2, 1.0};
        }

        @Override
		public double getDistance() {
            return 0.1;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.904026219693, 0.016708646875, 0.065341261036, 0.013923872396,
                    0.011139097917, 0.910170587813, 0.013923872396, 0.064766441875,
                    0.052273008829, 0.016708646875, 0.917094471901, 0.013923872396,
                    0.011139097917, 0.077719730250, 0.013923872396, 0.897217299437
            };
        }
    };

    UnequalBaseFrequencies[] all = {test1, test2};

    public void testHKY() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f); // "estimate", true

            HKY hky = new HKY();
            RealParameter rates = new RealParameter(test.getRates());
            hky.initByName("rates", rates, "frequencies", freqs);
            hky.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + hky.getSubstitution(i) + " : " + hky.getRate(i));

            // AC=AT=CG=GT
            assertEquals(true, hky.getRateAC()==hky.getRateAT() &&
                    hky.getRateAC()==hky.getRateCG() && hky.getRateAC()== hky.getRateGT());
            // AG=CT
            assertEquals(true, hky.getRateAG()==hky.getRateCT() &&
                    hky.getRateAC()!=hky.getRateCT() );

            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            hky.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();

            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}