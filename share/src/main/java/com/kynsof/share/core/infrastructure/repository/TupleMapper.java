package com.kynsof.share.core.infrastructure.repository;

import jakarta.persistence.Tuple;

public interface TupleMapper<T> {

    T map(Tuple tuple, IndexRef index);

    default T map(Tuple tuple, IndexRef index, boolean includeParent){
        return map(tuple, index);
    }

    int totalFields();
}
