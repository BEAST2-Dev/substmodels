package substmodels.nucleotide;
import beast.base.core.Description;

@Description("Standard nucleotide substitution model TIM1uf\nHas rates 012230 and estimated frequencies")
public class TIM1uf extends Base {
	
	public TIM1uf() {
		super("012230", false);
	}

}
