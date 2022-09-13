package com.cNerds.dailyMoment.like.service;

import com.cNerds.dailyMoment.core.service.GenericService;
import com.cNerds.dailyMoment.like.dao.LikeInfoDao;
import com.cNerds.dailyMoment.like.dto.LikeInfo;
import com.cNerds.dailyMoment.like.dto.LikeInfoCriterion;

public interface LikeInfoService extends GenericService<LikeInfo, LikeInfoCriterion, LikeInfoDao> {
    

}