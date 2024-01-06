package abistech.resseract.pipeline.executor;

import abistech.resseract.config.Config;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.pipeline.Pipeline;
import abistech.resseract.step.elements.Dataset;

/**
 * @author abisTarun
 *
 */
@FunctionalInterface
public interface PipelineExecutor {

	Dataset execute(Pipeline pipeline, Config config) throws ResseractException;
}
