package abistech.resseract.analysis;

import abistech.resseract.pipeline.Pipeline;
import abistech.resseract.auth.Feature;

public interface Analysis {
	AnalysisType getAnalysisType();
	Pipeline getPipeline();
	Feature getAuthorizedFeature();
}
