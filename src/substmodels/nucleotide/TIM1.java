package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TIM1<br>Has rates 012230 and equal frequencies")
public class TIM1 extends Base {
	
	public TIM1() {
		super("012230", true);
	}

}
