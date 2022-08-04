package com.cNerds.dailyMoment.core.dao.myBatis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MyBatisGenericDao<T, C> implements MyBatisDao<T, C> {

    protected Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("defaultSession")
    protected SqlSession defaultSession;

    Class<T> domainClass;

    public MyBatisGenericDao() {
    }

    protected MyBatisGenericDao(Class<T> domainClass) {
        this.domainClass = domainClass;
    }

    @Override
    public void insert(T entity) {
        defaultSession.insert(domainClass.getName() + ".insert", entity);
    }

    @Override
    public void update(T entity) {
        defaultSession.update(domainClass.getName() + ".update", entity);
    }


    @Override
    public int delete(T entity) {
        return (Integer) defaultSession.delete(domainClass.getName() + ".delete", entity);
    }

    @Override
    public long listCount(C criterion) {
        return (Long) defaultSession.selectOne(domainClass.getName() + ".listCount", criterion);
    }

    @Override
    public List<T> list(C criterion) {
        return defaultSession.selectList(domainClass.getName() + ".list", criterion);
    }

    @Override
    public T detail(T entity) {
        return defaultSession.selectOne(domainClass.getName() + ".detail", entity);
    }

    @Override
    public boolean exist(T entity) {
        int result = defaultSession.selectOne(domainClass.getName() + ".exist", entity);
        return (Boolean) (result > 0) ? true : false;
    }


}