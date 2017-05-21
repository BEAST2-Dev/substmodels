package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model HKY<br>Has rates 010010 and estimated frequencies")
public class HKY extends Base {
	
	public HKY() {
		super("010010", false);
	}

}
