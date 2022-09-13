package com.cNerds.dailyMoment.reply.dao;

import org.springframework.stereotype.Repository;

import com.cNerds.dailyMoment.core.dao.GenericeDaoImpl;
import com.cNerds.dailyMoment.photo.dto.PhotoInfo;
import com.cNerds.dailyMoment.reply.dto.ReplyInfo;
import com.cNerds.dailyMoment.reply.dto.ReplyInfoCriterion;

@Repository
public class ReplyInfoDaoImpl extends GenericeDaoImpl<ReplyInfo,ReplyInfoCriterion> implements ReplyInfoDao {

	public ReplyInfoDaoImpl() {
        super(ReplyInfo.class);
    }

	@Override
	public int searchReplyFamilyNo(int replyNo) {
		return defaultSession.selectOne(ReplyInfo.class.getName() + ".searchReplyFamilyNo",replyNo);
	}
}
