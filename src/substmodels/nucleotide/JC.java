package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model JC<br>Has rates 000000 and equal frequencies")
public class JC extends Base {
	
	public JC() {
		super("000000", true);
	}

}
