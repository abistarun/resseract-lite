package abistech.resseract.pipeline.executor;

import abistech.resseract.pipeline.executor.impl.DefaultPipelineExecutor;
import abistech.resseract.analysis.Analysis;

/**
 * @author abisTarun
 */
public class PipelineExecutionEngine {

	private PipelineExecutionEngine() {
	}

	public static PipelineExecutor getExecutor(Analysis ignoredAnalysis) {
		return new DefaultPipelineExecutor();
	}
}
