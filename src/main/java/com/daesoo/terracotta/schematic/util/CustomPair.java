package com.daesoo.terracotta.schematic.util;

import java.util.Objects;

public class CustomPair<L, R> {
    public L position;
    public R blockData;

    public CustomPair(L position, R blockData) {
        this.position = position;
        this.blockData = blockData;
    }

    public L getPosition() {
        return position;
    }

    public R getBlockData() {
        return blockData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomPair<?, ?> pair = (CustomPair<?, ?>) o;

        if (!Objects.equals(position, pair.position)) return false;
        return Objects.equals(blockData, pair.blockData);
    }

    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + (blockData != null ? blockData.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CustomPair[" + position + ", " + blockData + ']';
    }
}