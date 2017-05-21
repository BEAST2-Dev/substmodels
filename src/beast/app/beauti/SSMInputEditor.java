package beast.app.beauti;

import javax.swing.Box;
import javax.swing.JLabel;

import beast.app.draw.BEASTObjectInputEditor;
import beast.core.BEASTInterface;
import beast.core.Input;

public class SSMInputEditor extends BEASTObjectInputEditor {
	private static final long serialVersionUID = 1L;

	public SSMInputEditor(BeautiDoc doc) {
		super(doc);
	}
	
	@Override
	public Class<?> type() {
		return substmodels.nucleotide.Base.class;
	}
	
	@Override
	public void init(Input<?> input, BEASTInterface beastObject, int itemNr, ExpandOption isExpandOption,
			boolean addButtons) {
		super.init(input, beastObject, itemNr, isExpandOption, addButtons);
		Object o = getComponent(1);
		if (o instanceof Box) {
			o = ((Box)o).getComponent(0);
			if (o instanceof Box) {
				String label = ((BEASTInterface)input.get()).getDescription();
				label = label.replaceAll("\\n", "<br/>");
				((Box)o).add(new JLabel("<html>" + label + "</html>"));
			}
		}
	}

}
