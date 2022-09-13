package com.cNerds.dailyMoment.photo.dao;

import com.cNerds.dailyMoment.core.dao.GenericDao;
import com.cNerds.dailyMoment.photo.dto.PhotoInfo;
import com.cNerds.dailyMoment.photo.dto.PhotoInfoCriterion;

public interface PhotoInfoDao extends GenericDao<PhotoInfo, PhotoInfoCriterion> {

	int searchPhotoFamilyNo(int photoNo);

	int searchMaxPhotoNo();

	 
}
