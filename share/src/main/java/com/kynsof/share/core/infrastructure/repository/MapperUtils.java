package com.kynsof.share.core.infrastructure.repository;

public class MapperUtils {

    public MapperUtils(){

    }

    public static <T> T skip(IndexRef index, int count) {
        index.skip(count);
        return null;
    }
}
