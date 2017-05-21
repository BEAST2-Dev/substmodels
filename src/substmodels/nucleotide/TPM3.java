package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TPM3<br>Has rates 012012 and equal frequencies")
public class TPM3 extends Base {
	
	public TPM3() {
		super("012012", true);
	}

}
