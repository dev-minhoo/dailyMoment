package com.cNerds.dailyMoment.core.service;

import java.util.List;

import com.cNerds.dailyMoment.core.dao.GenericDao;
import com.cNerds.dailyMoment.user.dto.UserInfo;


public interface GenericService<T, C, D extends GenericDao<T,C>>{
	
    void insert(T entity, UserInfo loginUserInfo);

    void update(T entity, UserInfo loginUserInfo);

    int delete(T entity);

    List<T> list(C criterion);

    T detail(T entity);
}