package com.cNerds.dailyMoment.reply.dto;

import java.util.List;

import com.cNerds.dailyMoment.core.entity.EntityInfoImpl;
import com.cNerds.dailyMoment.like.dto.LikeInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class ReplyInfo extends EntityInfoImpl {
	
	private int replyNo;
	private int photoNo;
	private int parentReplyNo;
	private String comment;
	
	private String enReplyNo;
	
	private List<ReplyInfo> sonReplyList;
	
	private List<LikeInfo> likeList;
	
	
	
	
	
}
