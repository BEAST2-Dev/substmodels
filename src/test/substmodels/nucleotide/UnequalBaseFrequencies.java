package test.substmodels.nucleotide;

/**
 * @author Walter Xie
 */
public interface UnequalBaseFrequencies {
    Double[] getPi();

    Double [] getRates();

    double getDistance();

    double[] getExpectedResult();
}
