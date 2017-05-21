package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TrNef<br>Has rates 010020 and equal frequencies")
public class TrNef extends Base {
	
	public TrNef() {
		super("010020", true);
	}

}
