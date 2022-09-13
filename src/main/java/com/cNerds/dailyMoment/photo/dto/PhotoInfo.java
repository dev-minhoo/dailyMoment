package com.cNerds.dailyMoment.photo.dto;

import java.util.Date;
import java.util.List;

import com.cNerds.dailyMoment.core.entity.EntityInfoImpl;
import com.cNerds.dailyMoment.like.dto.LikeInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class PhotoInfo extends EntityInfoImpl {
	
	private int photoNo;
	private int childrenNo;
	private String imageName;
	private Date photoDate;
	private String latitude;
	private String hardness;
	private String comment;
	private int isMain;
	
	private String enPhotoNo;
	
	private List<LikeInfo> likeList;
	
	
	
	
	
}
