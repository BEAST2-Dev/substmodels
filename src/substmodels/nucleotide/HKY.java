package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model HKY\nHas rates 010010 and estimated frequencies")
public class HKY extends Base {
	
	public HKY() {
		super("010010", false);
	}

}
