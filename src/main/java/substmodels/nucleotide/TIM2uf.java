package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TIM2uf\nHas rates 010232 and estimated frequencies")
public class TIM2uf extends Base {
	
	public TIM2uf() {
		super("010232", false);
	}

}
