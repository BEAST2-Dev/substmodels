package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TIM1uf<br>Has rates 012230 and estimated frequencies")
public class TIM1uf extends Base {
	
	public TIM1uf() {
		super("012230", false);
	}

}
