package abistech.resseract.pipeline.executor.impl;

import abistech.resseract.config.Config;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.pipeline.Pipeline;
import abistech.resseract.pipeline.executor.PipelineExecutor;
import abistech.resseract.step.Step;
import abistech.resseract.step.elements.Dataset;

/**
 * @author abisTarun
 */
public class DefaultPipelineExecutor implements PipelineExecutor {

    @Override
    public Dataset execute(Pipeline pipeline, Config config) throws ResseractException {
        Dataset prevData = null;
        for (Step step : pipeline.getSteps()) {
            prevData = step.execute(prevData, config);
        }
        return prevData;
    }
}
