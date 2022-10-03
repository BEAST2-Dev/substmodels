package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TPM3uf\nHas rates 012012 and estimated frequencies")
public class TPM3uf extends Base {
	
	public TPM3uf() {
		super("012012", false);
	}

}
