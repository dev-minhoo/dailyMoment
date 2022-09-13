package com.cNerds.dailyMoment.children.service;

import com.cNerds.dailyMoment.children.dao.ChildrenInfoDao;
import com.cNerds.dailyMoment.children.dto.ChildrenInfo;
import com.cNerds.dailyMoment.children.dto.ChildrenInfoCriterion;
import com.cNerds.dailyMoment.core.service.GenericService;

public interface ChildrenInfoService extends GenericService<ChildrenInfo, ChildrenInfoCriterion,ChildrenInfoDao> {

	int searchMaxChildrenNo();
    

}