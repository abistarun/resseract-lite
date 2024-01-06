package abistech.resseract.step.elements;

import abistech.resseract.data.frame.Data;
import abistech.resseract.metadata.Metadata;
import abistech.resseract.metadata.MetadataElement;

/**
 * @author abisTarun
 */
public interface Dataset {

	void setData(Data data);

	Data getData();

	void addMetadata(MetadataElement metadataElement);

	Metadata getMetadata();

	void setMetadata(Metadata metadata);
}
