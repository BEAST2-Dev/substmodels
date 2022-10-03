package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TIM1\nHas rates 012230 and equal frequencies")
public class TIM1 extends Base {
	
	public TIM1() {
		super("012230", true);
	}

}
