package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TVM\nHas rates 012314 and estimated frequencies")
public class TVM extends Base {
	
	public TVM() {
		super("012314", false);
	}

}
