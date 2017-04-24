package com.pwr.projekt.enduroracepilot.MVP.repository.impl;

/**
 * Created by Sebastian on 2017-04-20.
 */

public interface Repository<T> {
    void add(T item);

    void add(Iterable<T> items);

    void update(T item);

    void remove(T item);

//    void remove(Specification specification);

//    List<T> query(Specification specification);
}