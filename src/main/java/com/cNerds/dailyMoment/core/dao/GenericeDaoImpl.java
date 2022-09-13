package com.cNerds.dailyMoment.core.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cNerds.dailyMoment.core.entity.EntityCriterion;
import com.cNerds.dailyMoment.core.entity.EntityInfo;


public class GenericeDaoImpl<T extends EntityInfo, C extends EntityCriterion>  implements GenericDao<T,C>{

	protected Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("sqlSessionTemplate")
    protected SqlSession defaultSession;

    Class<T> domainClass;

    public GenericeDaoImpl() {
    }

    protected GenericeDaoImpl(Class<T> domainClass) {
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
    public List<T> list(C criterion) {
        return defaultSession.selectList(domainClass.getName() + ".list", criterion);
    }

    @Override
    public T detail(T entity) {
        return defaultSession.selectOne(domainClass.getName() + ".detail", entity);
    }
}
