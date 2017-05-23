package test.substmodels.nucleotide;


import beast.core.Description;
import beast.core.parameter.RealParameter;
import beast.evolution.substitutionmodel.Frequencies;
import junit.framework.TestCase;
import substmodels.nucleotide.GTR;

/**
 * Test GTR matrix exponentiation
 *
 */
@Description("Test GTR matrix exponentiation")
public class GTRTest extends TestCase {

    /*
     * import numpy as np
     * from scipy.linalg import expm
     *
     * piQ = np.diag([.2, .3, .25, .25])
     * d = 0.1
     * # Q matrix with zeroed diagonal
     * XQ = np.matrix([[0, 0.2, 10, .3], [0.2, 0, 0.4, 5], [10, 0.4, 0, 0.5], [0.3, 5, 0.5, 0]])
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
            return new Double[]{0.25, 0.25, 0.25, 0.25};
        }

        @Override
		public Double [] getRates() {
            return new Double[] {0.5, 1.0, 0.5, 0.5, 1.0, 0.5};
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

    protected UnequalBaseFrequencies test1 = new UnequalBaseFrequencies() {
        @Override
		public Double[] getPi() {
            return new Double[]{0.50, 0.20, 0.2, 0.1};
        }

        @Override
		public Double [] getRates() {
            return new Double[] {0.5, 1.0, 0.5, 0.5, 1.0, 0.5};
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
            return new Double[] {0.2, 1.0, 0.2, 0.2, 1.0, 0.2};
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

    protected UnequalBaseFrequencies test3 = new UnequalBaseFrequencies() {
        @Override
		public Double[] getPi() {
            return new Double[]{0.20, 0.30, 0.25, 0.25};
        }

        @Override
		public Double [] getRates() {
            return new Double[] {0.2, 1.0, 0.3, 0.4, 1.0, 0.5};
        }

        @Override
		public double getDistance() {
            return 0.1;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
            		0.9151233523912986, 0.01419463331835106, 0.053614529507541434, 0.017067484782809166, 
            		0.009463088878900653, 0.9148659231065082, 0.022324155452048293, 0.05334683256254297, 
            		0.042891623606033207, 0.026788986542458024, 0.9028769239489847, 0.027442465902524332, 
            		0.01365398782624723, 0.06401619907505152, 0.027442465902524263, 0.8948873471961769
            };
        }
    };
    
    protected UnequalBaseFrequencies test4 = new UnequalBaseFrequencies() {
        @Override
		public Double[] getPi() {
            return new Double[]{0.20, 0.30, 0.25, 0.25};
        }

        @Override
		public Double [] getRates() {
            return new Double[] {0.2, 10.0, 0.3, 0.4, 5.0, 0.5};
        }

        @Override
		public double getDistance() {
            return 0.1;
        }

        @Override
		public double[] getExpectedResult() {
            return new double[]{
            			0.8780963047046206, 0.0033252855682803723, 0.11461112844510626, 0.003967281281992822, 
            			0.002216857045520258, 0.9327483979953872, 0.005055665025823634, 0.05997907993326873, 
            			0.09168890275608481, 0.006066798030988321, 0.8959983003009074, 0.0062459989120190644, 
            			0.0031738250255942332, 0.07197489591992245, 0.006245998912019033, 0.9186052801424642
            };
        }
    };


    UnequalBaseFrequencies[] all = {test4, test3, test2, test1, test0};

    public void testGTR() throws Exception {
        for (UnequalBaseFrequencies test : all) {

            RealParameter f = new RealParameter(test.getPi());
            Frequencies freqs = new Frequencies();
            freqs.initByName("frequencies", f);

            GTR gtr = new GTR();
            RealParameter gtrRates = new RealParameter(test.getRates());
            gtr.initByName("rates", gtrRates, "frequencies", freqs);
            gtr.printQ(System.out); // to obtain XQ for python script
//            for (int i = 0; i < 6; ++i)
//                System.out.println("Rate " + gtr.getSubstitution(i) + " : " + gtr.getRate(i));

            double distance = test.getDistance();

            double[] mat = new double[4 * 4];
            gtr.getTransitionProbabilities(null, distance, 0, 1, mat);

            final double[] result = test.getExpectedResult();

            for (int k = 0; k < mat.length; ++k) {
                assertEquals(mat[k], result[k], 1e-10);
                System.out.println(k + " : " + (mat[k] - result[k]));
            }
        }
    }
}