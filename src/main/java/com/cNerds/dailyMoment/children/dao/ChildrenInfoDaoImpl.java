package com.cNerds.dailyMoment.children.dao;

import org.springframework.stereotype.Repository;

import com.cNerds.dailyMoment.children.dto.ChildrenInfo;
import com.cNerds.dailyMoment.children.dto.ChildrenInfoCriterion;
import com.cNerds.dailyMoment.core.dao.GenericeDaoImpl;

@Repository
public class ChildrenInfoDaoImpl extends GenericeDaoImpl<ChildrenInfo,ChildrenInfoCriterion> implements ChildrenInfoDao {

	public ChildrenInfoDaoImpl() {
        super(ChildrenInfo.class);
    }

	@Override
	public int searchMaxChildrenNo() {
		return defaultSession.selectOne(ChildrenInfo.class.getName() + ".searchMaxChildrenNo");
	}

	 
}
