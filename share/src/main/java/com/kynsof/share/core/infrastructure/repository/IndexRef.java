package com.kynsof.share.core.infrastructure.repository;

public class IndexRef {
    public int value;

    public IndexRef(int initialValue) {
        this.value = initialValue;
    }

    public int next() {
        return value++;
    }

    public void skip(int count) {
        value += count;
    }

    public int get(){
        return value;
    }
}
