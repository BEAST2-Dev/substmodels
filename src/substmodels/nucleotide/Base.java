package substmodels.nucleotide;

import java.lang.reflect.InvocationTargetException;

import beast.core.Description;
import beast.core.Function;
import beast.core.Input.Validate;
import beast.core.Param;
import beast.core.parameter.RealParameter;
import beast.evolution.datatype.DataType;
import beast.evolution.datatype.Nucleotide;
import beast.evolution.substitutionmodel.Frequencies;
import beast.evolution.substitutionmodel.GeneralSubstitutionModel;

@Description("Reversible nucleotide substitution model parameterised by model number")
public class Base extends GeneralSubstitutionModel {
	String modelID;
	Boolean equalFreqs;

	public Boolean hasEqualFreqs() {
		return equalFreqs;
	}

	public void setEqualFreqs(Boolean equalFreqs) {
		this.equalFreqs = equalFreqs;
	}

	public String getModelID() {
		return modelID;
	}

	public void setModelID(String modelID) {
		this.modelID = modelID;
	}

	public Base(@Param(name="mode", description="model identifier is a 6 digit number describing which parameters are linked. "
			+ "For instance 010010 = HKY, 012345 = GTR. "
			+ "Order of digits are for rates AC, AG, AT, CG, CT, GT respectively ") String modelID,
			@Param(name="equalFreqs", description="") Boolean equalFreqs) {
		frequenciesInput.setRule(Validate.OPTIONAL);
		this.modelID = modelID;
		this.equalFreqs = equalFreqs;
	}
	
	int [] modelMap;
	
	@Override
	public void initAndValidate() {
        frequencies = frequenciesInput.get();
        if (!equalFreqs && frequencies == null) {
        	throw new IllegalArgumentException("Frequencies must be specified");
        }
        if (equalFreqs) {
        	RealParameter f = new RealParameter("0.25 0.25 0.25 0.25");
        	frequencies = new Frequencies();
        	frequencies.initByName("frequencies", f);
        }
        updateMatrix = true;
        nrOfStates = 4;

        if (modelID.length() != 6) {
        	throw new IllegalArgumentException("modelID must be 6 digits, but got " + modelID.length());
        }
        modelMap = new int[6];
        
        int max = 0;
        for (int i = 0; i < 6; i++) {
        	modelMap[i] = modelID.charAt(i) - '0';
        	if (modelMap[i] <=0 || modelMap[i] > 5) {
            	throw new IllegalArgumentException("modelID must be 6 digits, but got a wrong character at position " + i + ": " + modelID);
        	}
        	max = Math.max(max, modelMap[i]);
        }
        
        if (ratesInput.get().getDimension() != max * (max - 1)) {
            throw new IllegalArgumentException("Dimension of input 'rates' is " + ratesInput.get().getDimension() + " but a " +
                    "rate matrix of dimension " + max + "x" + (max - 1) + "=" + max * (nrOfStates - 1) + " was " +
                    "expected");
        }

        try {
			eigenSystem = createEigenSystem();
		} catch (SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

        rateMatrix = new double[nrOfStates][nrOfStates];
        relativeRates = new double[nrOfStates * (nrOfStates-1)];
        storedRelativeRates = new double[nrOfStates * (nrOfStates-1)];
	}
	
	
	@Override
	protected void setupRelativeRates() {
        Function rates = this.ratesInput.get();
        relativeRates[0] = rates.getArrayValue(modelMap[0]);
        relativeRates[1] = rates.getArrayValue(modelMap[1]);
        relativeRates[2] = rates.getArrayValue(modelMap[2]);
        relativeRates[3] = rates.getArrayValue(modelMap[0]);
        relativeRates[4] = rates.getArrayValue(modelMap[3]);
        relativeRates[5] = rates.getArrayValue(modelMap[4]);
        relativeRates[6] = rates.getArrayValue(modelMap[1]);
        relativeRates[7] = rates.getArrayValue(modelMap[3]);
        relativeRates[8] = rates.getArrayValue(modelMap[5]);
        relativeRates[9] = rates.getArrayValue(modelMap[2]);
        relativeRates[10] = rates.getArrayValue(modelMap[4]);
        relativeRates[11] = rates.getArrayValue(modelMap[5]);
    }

	
	@Override
	public boolean canHandleDataType(DataType dataType) {
        return dataType instanceof Nucleotide;
	}
}
