package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TPM1uf<br>Has rates 012210 and estimated frequencies")
public class TPM1uf extends Base {
	
	public TPM1uf() {
		super("012210", false);
	}

}
