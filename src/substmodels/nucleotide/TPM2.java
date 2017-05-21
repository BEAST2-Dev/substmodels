package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TPM2<br>Has rates 010212 and equal frequencies")
public class TPM2 extends Base {
	
	public TPM2() {
		super("010212", true);
	}

}
