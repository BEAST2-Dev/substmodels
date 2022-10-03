package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TPM1uf\nHas rates 012210 and estimated frequencies")
public class TPM1uf extends Base {
	
	public TPM1uf() {
		super("012210", false);
	}

}
