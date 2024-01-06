package abistech.resseract.step.impl.processing;

import abistech.resseract.config.Boolean;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.Data;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.step.elements.Dataset;
import abistech.resseract.util.Util;
import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.ColumnNames;
import abistech.resseract.step.impl.AbstractStep;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sort extends AbstractStep {

    @Override
    public Dataset execute(Dataset input, Config config) throws ResseractException {
        String sortColumn = (String) config.get(ConfigKey.SORT_COLUMN);
        Data data = input.getData();

        if (!Util.isValidString(sortColumn))
            sortColumn = data.getPrimaryColumnName();

        if (!data.getColumnNames().contains(sortColumn))
            throw new ResseractException(CustomErrorReports.INVALID_SORT_COLUMN, data.getColumnNames());

        Column<?> column = data.getColumn(sortColumn);
        List<Integer> sortIndicies = column.getSortIndices();

        if (!Boolean.valueOf((String) config.get(ConfigKey.SORT_ASCENDING)).isValue())
            Collections.reverse(sortIndicies);
        data.rearrange(sortIndicies);
        return input;
    }

    @Override
    public List<ConfigKey> getRequiredConfigs() {
        return Arrays.asList(ConfigKey.SORT_COLUMN, ConfigKey.SORT_ASCENDING);
    }
}
