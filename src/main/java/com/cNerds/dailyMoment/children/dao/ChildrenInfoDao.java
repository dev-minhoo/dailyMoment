package com.cNerds.dailyMoment.children.dao;

import com.cNerds.dailyMoment.children.dto.ChildrenInfo;
import com.cNerds.dailyMoment.children.dto.ChildrenInfoCriterion;
import com.cNerds.dailyMoment.core.dao.GenericDao;

public interface ChildrenInfoDao extends GenericDao<ChildrenInfo, ChildrenInfoCriterion> {

	int searchMaxChildrenNo();

	 
}
