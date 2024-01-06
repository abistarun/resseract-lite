package abistech.resseract.step;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.step.elements.Dataset;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * @author abisTarun
 */
public interface Step {

	Dataset execute(Dataset input, Config config) throws ResseractException;

	@JsonIgnore
	List<ConfigKey> getRequiredConfigs();
}
