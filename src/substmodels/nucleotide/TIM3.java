package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TIM3\nHas rates 012032 and equal frequencies")
public class TIM3 extends Base {
	
	public TIM3() {
		super("012032", true);
	}

}
