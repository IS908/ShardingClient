package com.sharding.client.router.util;

/**
 * column ->node index
 *
 * @author wuzhih
 */
public class ColumnRoutePair {
    public final String colValue;
    public final RangeValue rangeValue;
    public final boolean isRange;

    private int slot = -2;

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ColumnRoutePair(String colValue) {
        super();
        this.colValue = colValue;
        this.rangeValue = null;
        this.isRange = false;
    }

    public ColumnRoutePair(RangeValue rangeValue) {
        super();
        this.rangeValue = rangeValue;
        this.colValue = null;
        this.isRange = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((colValue == null) ? 0 : colValue.hashCode());
        result = prime * result + ((rangeValue == null) ? 0 : rangeValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ColumnRoutePair other = (ColumnRoutePair) obj;
        if (colValue == null) {
            if (other.colValue != null) {
                return false;
            }
        } else if (!colValue.equals(other.colValue)) {
            return false;
        }

        if (rangeValue == null) {
            return other.rangeValue == null;
        } else return rangeValue.equals(other.rangeValue);
    }

    @Override
    public String toString() {
        return "ColumnRoutePair [colValue=" + colValue + "";
    }
}
