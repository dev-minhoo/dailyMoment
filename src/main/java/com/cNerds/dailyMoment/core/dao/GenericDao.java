package com.cNerds.dailyMoment.core.dao;

import java.util.List;

public interface GenericDao<T, C> {
	void insert(T entity);

    void update(T entity);

    int delete(T entity);

    List<T> list(C criterion);

    T detail(T entity);
}
