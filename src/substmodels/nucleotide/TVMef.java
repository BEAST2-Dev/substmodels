package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TVMef\nHas rates 012314 and equal frequencies")
public class TVMef extends Base {
	
	public TVMef() {
		super("012314", true);
	}

}
