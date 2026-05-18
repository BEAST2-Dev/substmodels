package test.substmodels.nucleotide;

/**
 * @author Walter Xie
 */
public interface UnequalBaseFrequencies {
    double[] getPi();

    double [] getRates();

    double getDistance();

    double[] getExpectedResult();
}
