package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model SYM<br>Has rates 012345 and equal frequencies")
public class SYM extends Base {
	
	public SYM() {
		super("012345", true);
	}

}
