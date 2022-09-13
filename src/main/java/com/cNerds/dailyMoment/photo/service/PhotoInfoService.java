package com.cNerds.dailyMoment.photo.service;

import com.cNerds.dailyMoment.core.service.GenericService;
import com.cNerds.dailyMoment.photo.dao.PhotoInfoDao;
import com.cNerds.dailyMoment.photo.dto.PhotoInfo;
import com.cNerds.dailyMoment.photo.dto.PhotoInfoCriterion;

public interface PhotoInfoService extends GenericService<PhotoInfo, PhotoInfoCriterion, PhotoInfoDao> {

	int searchPhotoFamilyNo(int photoNo);

	int searchMaxPhotoNo();
    

}