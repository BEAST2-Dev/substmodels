package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TVM<br>Has rates 012314 and estimated frequencies")
public class TVM extends Base {
	
	public TVM() {
		super("012314", false);
	}

}
