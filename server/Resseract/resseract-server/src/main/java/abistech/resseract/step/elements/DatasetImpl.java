package abistech.resseract.step.elements;

import abistech.resseract.data.frame.Data;
import abistech.resseract.metadata.Metadata;
import abistech.resseract.metadata.MetadataElement;

/**
 * @author abisTarun
 */
public class DatasetImpl implements Dataset {

	private Data data;
	private Metadata metadata;

	public DatasetImpl(Data data) {
		this.data = data;
		this.metadata = new Metadata();
	}

	public DatasetImpl() {
	}

	@Override
	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public Data getData() {
		return data;
	}

	@Override
	public void addMetadata(MetadataElement metadataElement) {
		metadata.add(metadataElement);
	}

	@Override
	public Metadata getMetadata() {
		return metadata;
	}

	@Override
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
}
