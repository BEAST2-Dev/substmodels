package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TVMef<br>Has rates 012314 and equal frequencies")
public class TVMef extends Base {
	
	public TVMef() {
		super("012314", true);
	}

}
