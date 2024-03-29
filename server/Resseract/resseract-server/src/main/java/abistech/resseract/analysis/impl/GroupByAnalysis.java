package abistech.resseract.analysis.impl;

import abistech.resseract.analysis.AbstractAnalysis;
import abistech.resseract.analysis.AnalysisType;
import abistech.resseract.pipeline.Pipeline;
import abistech.resseract.auth.Feature;
import abistech.resseract.step.impl.data.DataLoader;
import abistech.resseract.step.impl.processing.*;

public class GroupByAnalysis extends AbstractAnalysis {
    @Override
    public AnalysisType getAnalysisType() {
        return AnalysisType.GROUP_BY;
    }

    @Override
    public Pipeline getPipeline() {
        Pipeline pipeline = new Pipeline();
        pipeline.addStep(new DataLoader());
        pipeline.addStep(new Slice());
        pipeline.addStep(new GroupBy());
        pipeline.addStep(new EvaluateExpression());
        pipeline.addStep(new Sort());
        pipeline.addStep(new TopN());
        return pipeline;
    }

    @Override
    public Feature getAuthorizedFeature() {
        return Feature.DESCRIPTIVE_ANALYTICS;
    }
}
