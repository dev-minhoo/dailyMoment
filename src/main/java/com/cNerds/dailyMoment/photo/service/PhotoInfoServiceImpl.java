package com.cNerds.dailyMoment.photo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cNerds.dailyMoment.core.service.GenericServiceImpl;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.like.dto.LikeInfo;
import com.cNerds.dailyMoment.like.dto.LikeInfoCriterion;
import com.cNerds.dailyMoment.like.service.LikeInfoService;
import com.cNerds.dailyMoment.photo.dao.PhotoInfoDao;
import com.cNerds.dailyMoment.photo.dto.PhotoInfo;
import com.cNerds.dailyMoment.photo.dto.PhotoInfoCriterion;

@Service
public class PhotoInfoServiceImpl extends GenericServiceImpl<PhotoInfo, PhotoInfoCriterion, PhotoInfoDao> implements PhotoInfoService {
	
	@Autowired
	private LikeInfoService likeInfoService;
	 
	public PhotoInfoServiceImpl() {
        super(PhotoInfoDao.class);
    }
		
	@Override
	public List<PhotoInfo> list(PhotoInfoCriterion photoInfoCriterion) {
		List<PhotoInfo> photoInfoList = dao.list(photoInfoCriterion);
		if(!photoInfoList.isEmpty()) {
			for(PhotoInfo item : photoInfoList) {
				item.setEnPhotoNo(CryptoUtil.encode(Integer.toString(item.getPhotoNo())));
				// 좋아요
				LikeInfoCriterion likeInfoCriterion = new LikeInfoCriterion();
				likeInfoCriterion.setSearchPhotoNo(item.getPhotoNo());
				List<LikeInfo> likeList = likeInfoService.list(likeInfoCriterion);
				item.setLikeList(likeList);
			}
		}
		return photoInfoList;
	}
	
	@Override
	public PhotoInfo detail (PhotoInfo photoInfo) {
		photoInfo = dao.detail(photoInfo);
		photoInfo.setEnPhotoNo(CryptoUtil.encode(Integer.toString(photoInfo.getPhotoNo())));
		return photoInfo;
	}
	
	@Override
	public int searchPhotoFamilyNo(int photoNo) {
		return dao.searchPhotoFamilyNo(photoNo);
	}

	@Override
	public int searchMaxPhotoNo() {
		return dao.searchMaxPhotoNo();
	}
    
		
	
    

}