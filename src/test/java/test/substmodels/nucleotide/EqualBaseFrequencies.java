package test.substmodels.nucleotide;

/**
 * @author Walter Xie
 */
public interface EqualBaseFrequencies extends UnequalBaseFrequencies {
    @Override
    default double[] getPi() {
        throw new IllegalArgumentException("Default to [.25, .25, .25, .25] !");
    }
}
