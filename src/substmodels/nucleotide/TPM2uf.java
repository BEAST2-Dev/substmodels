package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TPM2uf<br>Has rates 010212 and estimated frequencies")
public class TPM2uf extends Base {
	
	public TPM2uf() {
		super("010212", false);
	}

}
