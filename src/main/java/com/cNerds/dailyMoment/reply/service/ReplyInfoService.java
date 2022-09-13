package com.cNerds.dailyMoment.reply.service;

import com.cNerds.dailyMoment.core.service.GenericService;
import com.cNerds.dailyMoment.reply.dao.ReplyInfoDao;
import com.cNerds.dailyMoment.reply.dto.ReplyInfo;
import com.cNerds.dailyMoment.reply.dto.ReplyInfoCriterion;

public interface ReplyInfoService extends GenericService<ReplyInfo, ReplyInfoCriterion, ReplyInfoDao> {

	int searchReplyFamilyNo(int replyNo);
    

}