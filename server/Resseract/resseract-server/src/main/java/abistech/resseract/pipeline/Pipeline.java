package abistech.resseract.pipeline;

import abistech.resseract.step.Step;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abisTarun
 */
@EqualsAndHashCode
@ToString
public class Pipeline {

	private final List<Step> steps = new ArrayList<>();

	public void addStep(Step step) {
		steps.add(step);
	}

	public List<Step> getSteps() {
		return steps;
	}
}
