package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TIM2\nHas rates 010232 and equal frequencies")
public class TIM2 extends Base {
	
	public TIM2() {
		super("010232", true);
	}

}
