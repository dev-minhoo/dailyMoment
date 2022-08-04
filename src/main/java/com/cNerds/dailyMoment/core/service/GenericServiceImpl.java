package com.cNerds.dailyMoment.core.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.cNerds.dailyMoment.core.dao.GenericDao;
import com.cNerds.dailyMoment.core.entity.EntityCriterion;
import com.cNerds.dailyMoment.core.entity.EntityInfo;
import com.cNerds.dailyMoment.core.util.ApplicationContextUtils;
import com.cNerds.dailyMoment.user.UserInfo;

public abstract class GenericServiceImpl<T extends EntityInfo, C extends EntityCriterion, D extends GenericDao<T, C>> 
        implements GenericService<T, C, D>, ApplicationContextAware, InitializingBean {

    protected Log logger = LogFactory.getLog(getClass());

    private ApplicationContext applicationContext;

    protected Class<D> daoClass;
    protected D dao;

    public GenericServiceImpl() {
    }

    protected GenericServiceImpl(Class<D> daoClass) {
        this.daoClass = daoClass;
    }

    /*
     * ApplicationContextAware interface implementation.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /*
     * InitializingBean interface implementation. Autowiring GenericDao by type.
     */
    public void afterPropertiesSet() throws Exception {
        if (dao == null)
            dao = (D) ApplicationContextUtils.getBeanByType(applicationContext, daoClass);
    }

    /*
     * 로그인 정보 셋팅
     */
    public void settingEntity(T entity , UserInfo loginUserInfo) {
        try{
            entity.setCreator(loginUserInfo);// 로그인정보
            entity.setModifier(loginUserInfo);// 로그인정보
        }catch(Exception e){
            //
        }
    }

    @Override
    public void insert(T entity ,UserInfo loginUserInfo) {
        settingEntity(entity,loginUserInfo);
        dao.insert(entity);
    }

    @Override
    public void update(T entity ,UserInfo loginUserInfo) {
    	settingEntity(entity ,loginUserInfo);
        dao.update(entity);
    }

    @Override
    public int delete(T entity) {
        return dao.delete(entity);
    }
    
    @Override
    public boolean exist(T entity) {
        return dao.exist(entity);
    }

    @Override
    public long listCount(C criterion) {
        return dao.listCount(criterion);
    }
    
    @Override
    public List<T> list(C criterion) {       
        return dao.list(criterion);
    }

    @Override
    public T detail(T entity) {
        return dao.detail(entity);
    }
}