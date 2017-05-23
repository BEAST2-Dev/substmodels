package test.substmodels.nucleotide;

import beast.core.Description;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import substmodels.nucleotide.K80;

/**
 * Test K80 matrix exponentiation
 */
@Description("Test K80 matrix exponentiation")
public class K80Test extends TestCase {

    protected EqualBaseFrequencies test0 = new EqualBaseFrequencies() {
        @Override
        public Double [] getRates() {
            return new Double[] {1.0, 2.0};
        }

        @Override
		public double getDistance() {
            return 0.1;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
                    0.906563342722, 0.023790645491, 0.045855366296, 0.023790645491,
                    0.023790645491, 0.906563342722, 0.023790645491, 0.045855366296,
                    0.045855366296, 0.023790645491, 0.906563342722, 0.023790645491,
                    0.023790645491, 0.045855366296, 0.023790645491, 0.906563342722
            };
        }
    };


    EqualBaseFrequencies[] all = {test0};

    public void testK80() throws Exception {
        for (EqualBaseFrequencies test : all) {

            K80 k80 = new K80();
            RealParameter rates = new RealParameter(test.getRates());
            k80.initByName("rates", rates);
            k80.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + k80.getSubstitution(i) + " : " + k80.getRate(i));

            // AC=AT=CG=GT
            assertEquals(true, k80.getRateAC()==k80.getRateAT() &&
                    k80.getRateAC()==k80.getRateCG() && k80.getRateAC()== k80.getRateGT());
            // AG=CT
            assertEquals(true, k80.getRateAG()==k80.getRateCT() &&
                    k80.getRateAC()!=k80.getRateCT() );
            
            double distance = test.getDistance();
            double[] mat = new double[4 * 4];
            k80.getTransitionProbabilities(null, distance, 0, 1, mat);
            final double[] result = test.getExpectedResult();

            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}