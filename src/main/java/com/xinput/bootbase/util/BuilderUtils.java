package com.xinput.bootbase.util;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Author: xinput
 * @Date: 2020-02-12 18:05
 * @since 1.8
 * <p>
 * 通过jdk的Builder模式构建器
 */
public class BuilderUtils<T> {

    private final Supplier<T> instantiator;

    private List<Consumer<T>> modifiers = Lists.newArrayList();

    public BuilderUtils(Supplier<T> instantiator) {
        this.instantiator = instantiator;
    }

    public static <T> BuilderUtils<T> of(Supplier<T> instantiator) {
        return new BuilderUtils<>(instantiator);
    }

    public <P1> BuilderUtils<T> with(Consumer1<T, P1> consumer, P1 p1) {
        Consumer<T> c = instance -> consumer.accept(instance, p1);
        modifiers.add(c);
        return this;
    }

    public <P1, P2> BuilderUtils<T> with(Consumer2<T, P1, P2> consumer, P1 p1, P2 p2) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
        modifiers.add(c);
        return this;
    }

    public T build() {
        T value = instantiator.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }

    /**
     * 1个参数 Consumer
     */
    @FunctionalInterface
    public interface Consumer1<T, P1> {
        void accept(T t, P1 p1);
    }

    /**
     * 2个参数 Consumer
     */
    @FunctionalInterface
    public interface Consumer2<T, P1, P2> {
        void accept(T t, P1 p1, P2 p2);
    }

}
