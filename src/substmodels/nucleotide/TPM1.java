package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TPM1\nHas rates 012210 and equal frequencies")
public class TPM1 extends Base {
	
	public TPM1() {
		super("012210", true);
	}

}
