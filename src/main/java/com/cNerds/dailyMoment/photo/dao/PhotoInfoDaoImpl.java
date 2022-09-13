package com.cNerds.dailyMoment.photo.dao;

import org.springframework.stereotype.Repository;

import com.cNerds.dailyMoment.core.dao.GenericeDaoImpl;
import com.cNerds.dailyMoment.photo.dto.PhotoInfo;
import com.cNerds.dailyMoment.photo.dto.PhotoInfoCriterion;

@Repository
public class PhotoInfoDaoImpl extends GenericeDaoImpl<PhotoInfo,PhotoInfoCriterion> implements PhotoInfoDao {

	public PhotoInfoDaoImpl() {
        super(PhotoInfo.class);
    }

	@Override
	public int searchPhotoFamilyNo(int photoNo) {
		return defaultSession.selectOne(PhotoInfo.class.getName() + ".searchPhotoFamilyNo",photoNo);
	}

	@Override
	public int searchMaxPhotoNo() {
		return defaultSession.selectOne(PhotoInfo.class.getName() + ".searchMaxPhotoNo");
	}
	
}
