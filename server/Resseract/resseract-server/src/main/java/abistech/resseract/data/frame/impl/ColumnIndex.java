package abistech.resseract.data.frame.impl;

import abistech.resseract.data.frame.impl.column.DataType;

import java.util.Objects;

class ColumnIndex {
    private int index;
    private final DataType dataType;

    ColumnIndex(int index, DataType dataType) {
        this.index = index;
        this.dataType = dataType;
    }

    int getIndex() {
        return index;
    }

    void setIndex(int index) {
        this.index = index;
    }

    DataType getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnIndex that = (ColumnIndex) o;
        return index == that.index &&
                dataType == that.dataType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, dataType);
    }
}