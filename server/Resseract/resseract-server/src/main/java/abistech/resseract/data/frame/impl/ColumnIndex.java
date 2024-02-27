package abistech.resseract.data.frame.impl;

import abistech.resseract.data.frame.impl.column.DataType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
class ColumnIndex {
    private int index;
    private int globalIndex;
    private final DataType dataType;
}