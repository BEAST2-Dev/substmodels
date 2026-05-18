package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TrN\nHas rates 010020 and estimated frequencies")
public class TrN extends Base {
	
	public TrN() {
		super("010020", false);
	}

}
