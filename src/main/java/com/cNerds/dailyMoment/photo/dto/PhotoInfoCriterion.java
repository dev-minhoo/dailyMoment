package com.cNerds.dailyMoment.photo.dto;

import java.util.Date;

import com.cNerds.dailyMoment.core.entity.EntityCriterionImpl;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class PhotoInfoCriterion extends EntityCriterionImpl {
	private Integer searchPhotoNo;
	private Date searchPhotoDate;
	private Integer searchLikePhotoUserNo;
	
}
