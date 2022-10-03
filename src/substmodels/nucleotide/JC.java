package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model JC\nHas rates 000000 and equal frequencies")
public class JC extends Base {
	
	public JC() {
		super("000000", true);
	}

}
