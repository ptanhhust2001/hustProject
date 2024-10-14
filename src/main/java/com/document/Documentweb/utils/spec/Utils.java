package com.document.Documentweb.utils.spec;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class Utils {
    private Utils() {
    }

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static <S, T> List<T> mapList(ModelMapper modelMapper, List<S> source, Class<T> targetClass) {
        return source.stream().map(element -> modelMapper.map(element, targetClass)).toList();
    }
}
