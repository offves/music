package com.offves.music.common.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Assert;

import java.util.*;

public class BeanCopyUtil {

    private static final Map<Pair<Class<?>, Class<?>>, BeanCopier> cache = new HashMap<>();

    public static <T> T copy(Object source, Class<T> target) {
        Assert.notNull(source, "source must not be null");
        BeanCopier copier = cache.computeIfAbsent(Pair.of(source.getClass(), target), pair -> BeanCopier.create(pair.getKey(), target, false));
        try {
            T o = target.newInstance();
            copier.copy(source, o, null);
            return o;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> List<T> copyList(Collection<?> sources, Class<T> target) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        List<T> collection = new ArrayList<>();
        sources.forEach(source -> collection.add(copy(source, target)));
        return collection;
    }

}
