package test.substmodels.nucleotide;

/**
 * @author Walter Xie
 */
public interface EqualBaseFrequencies extends UnequalBaseFrequencies {
    @Override
    default Double[] getPi() {
        throw new IllegalArgumentException("Default to [.25, .25, .25, .25] !");
    }
}
