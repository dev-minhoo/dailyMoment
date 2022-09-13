package com.cNerds.dailyMoment.reply.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cNerds.dailyMoment.core.service.GenericServiceImpl;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.like.dto.LikeInfo;
import com.cNerds.dailyMoment.like.dto.LikeInfoCriterion;
import com.cNerds.dailyMoment.like.service.LikeInfoService;
import com.cNerds.dailyMoment.reply.dao.ReplyInfoDao;
import com.cNerds.dailyMoment.reply.dto.ReplyInfo;
import com.cNerds.dailyMoment.reply.dto.ReplyInfoCriterion;
import com.cNerds.dailyMoment.user.dto.UserInfo;

@Service
public class ReplyInfoServiceImpl extends GenericServiceImpl<ReplyInfo, ReplyInfoCriterion, ReplyInfoDao> implements ReplyInfoService {
	
	@Autowired
	private LikeInfoService likeInfoService; 
	
	public ReplyInfoServiceImpl() {
        super(ReplyInfoDao.class);
    }
		
	
	@Override
	public List<ReplyInfo> list(ReplyInfoCriterion replyInfoCriterion) {
		List<ReplyInfo> replyInfoList = dao.list(replyInfoCriterion);
		if(!replyInfoList.isEmpty()) {
			for(ReplyInfo item : replyInfoList) {
				
				item.setEnReplyNo(CryptoUtil.encode(Integer.toString(item.getReplyNo())));
				
				// 대댓글
				ReplyInfoCriterion sonReplyInfoCriterion = new ReplyInfoCriterion();
				sonReplyInfoCriterion.setSearchParentReplyNo(item.getReplyNo());
				List<ReplyInfo> sonReplyList = dao.list(sonReplyInfoCriterion);
				if(!sonReplyList.isEmpty()) {
					for(ReplyInfo item2 : sonReplyList) {
						item2.setEnReplyNo(CryptoUtil.encode(Integer.toString(item2.getReplyNo())));
						
						// 대댓글 좋아요
						LikeInfoCriterion likeInfoCriterion = new LikeInfoCriterion();
						likeInfoCriterion.setSearchReplyNo(item2.getReplyNo());
						List<LikeInfo> likeList = likeInfoService.list(likeInfoCriterion);
						item2.setLikeList(likeList);
					}
				}
				item.setSonReplyList(sonReplyList);
				
				// 좋아요
				LikeInfoCriterion likeInfoCriterion = new LikeInfoCriterion();
				likeInfoCriterion.setSearchReplyNo(item.getReplyNo());
				List<LikeInfo> likeList = likeInfoService.list(likeInfoCriterion);
				item.setLikeList(likeList);
				
			}
		}
		return replyInfoList;
	}
	
	@Override
	public ReplyInfo detail (ReplyInfo replyInfo) {
		replyInfo = dao.detail(replyInfo);
		replyInfo.setEnReplyNo(CryptoUtil.encode(Integer.toString(replyInfo.getReplyNo())));
		return replyInfo;
	}


	@Override
	public int searchReplyFamilyNo(int replyNo) {
		return dao.searchReplyFamilyNo(replyNo);
	}

    

}