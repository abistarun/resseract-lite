package abistech.resseract.analysis;

import abistech.resseract.data.frame.Data;
import abistech.resseract.metadata.Metadata;
import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.ColumnNames;
import abistech.resseract.step.elements.Dataset;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class AnalysisResult {

    private List<Object> index;
    private Map<String, AnalysisElement> columns;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Metadata metadata;

    public List<Object> getIndex() {
        return index;
    }

    public void setIndex(List<Object> index) {
        this.index = index;
    }

    public Map<String, AnalysisElement> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, AnalysisElement> columns) {
        this.columns = columns;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public static AnalysisResult build(Dataset dataset) {
        Data data = dataset.getData();
        AnalysisResult analysisResult = new AnalysisResult();
        if (data.getIndex() != null)
            analysisResult.index = new ArrayList<>(data.getIndex().toList());
        Map<String, AnalysisElement> columns = new LinkedHashMap<>();
        List<Column<?>> allColumns = data.getAllColumns();
        for (Column<?> column : allColumns) {
            if (data.getIndexName() != null && column.getName().equals(data.getIndexName()))
                continue;
            String colName = column.getName();
            List<?> colData = column.toList();
            columns.put(colName, new AnalysisElement(new ArrayList<>(colData)));
        }
        analysisResult.columns = columns;
        analysisResult.metadata = dataset.getMetadata();
        return analysisResult;
    }

    public static AnalysisResult build(List<Date> index, List<Double> column) {
        AnalysisResult analysisResult = new AnalysisResult();
        analysisResult.index = new ArrayList<>(index);
        analysisResult.columns = new HashMap<>(1);
        analysisResult.columns.put(ColumnNames.DATA, new AnalysisElement(new ArrayList<>(column)));
        return analysisResult;
    }

    public void append(AnalysisResult newAnalysisResult) {
        this.index.addAll(newAnalysisResult.index);
        for (Map.Entry<String, AnalysisElement> entry : newAnalysisResult.columns.entrySet()) {
            String key = entry.getKey();
            if (this.columns.containsKey(key))
                this.columns.get(key).getData().addAll(entry.getValue().getData());
            else
                this.columns.put(key, entry.getValue());
        }
    }
}
