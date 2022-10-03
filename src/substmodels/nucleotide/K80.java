package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model K80\nHas rates 010010 and equal frequencies")
public class K80 extends Base {
	
	public K80() {
		super("010010", true);
	}

}
