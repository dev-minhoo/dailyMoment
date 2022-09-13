package com.cNerds.dailyMoment.reply.dao;

import com.cNerds.dailyMoment.core.dao.GenericDao;
import com.cNerds.dailyMoment.reply.dto.ReplyInfo;
import com.cNerds.dailyMoment.reply.dto.ReplyInfoCriterion;

public interface ReplyInfoDao extends GenericDao<ReplyInfo, ReplyInfoCriterion> {

	int searchReplyFamilyNo(int replyNo);

	 
}
