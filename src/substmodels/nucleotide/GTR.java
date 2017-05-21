package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model GTR\nHas rates 012345 and estimated frequencies")
public class GTR extends Base {
	
	public GTR() {
		super("012345", false);
	}

}
