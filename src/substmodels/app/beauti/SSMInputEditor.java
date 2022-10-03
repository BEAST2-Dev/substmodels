package substmodels.app.beauti;

import beastfx.app.inputeditor.BEASTObjectInputEditor;
import beastfx.app.inputeditor.BeautiDoc;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import beast.base.core.BEASTInterface;
import beast.base.core.Input;

public class SSMInputEditor extends BEASTObjectInputEditor {

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
		Object o = pane;
		if (o instanceof HBox) {
			//o = ((HBox)o).getChildren().get(0);
			//if (o instanceof HBox) {
				String label = ((BEASTInterface)input.get()).getDescription();
				label = label.replaceAll("<br/>", "\\n");
				label = label.replaceAll("<br>", "\\n");
				((HBox)o).getChildren().add(new Label(label));
			//}
		}
	}

}
