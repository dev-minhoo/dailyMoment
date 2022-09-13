package com.cNerds.dailyMoment.like.dao;

import org.springframework.stereotype.Repository;

import com.cNerds.dailyMoment.core.dao.GenericeDaoImpl;
import com.cNerds.dailyMoment.like.dto.LikeInfo;
import com.cNerds.dailyMoment.like.dto.LikeInfoCriterion;

@Repository
public class LikeInfoDaoImpl extends GenericeDaoImpl<LikeInfo,LikeInfoCriterion> implements LikeInfoDao {

	public LikeInfoDaoImpl() {
        super(LikeInfo.class);
    }
	 
}
