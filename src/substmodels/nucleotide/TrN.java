package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TrN<br>Has rates 010020 and estimated frequencies")
public class TrN extends Base {
	
	public TrN() {
		super("010020", false);
	}

}
