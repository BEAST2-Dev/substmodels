package substmodels.nucleotide;
import beast.core.Description;

@Description("Standard nucleotide substitution model TIM3<br>Has rates 012032 and equal frequencies")
public class TIM3 extends Base {
	
	public TIM3() {
		super("012032", true);
	}

}
