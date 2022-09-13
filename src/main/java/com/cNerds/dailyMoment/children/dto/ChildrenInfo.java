package com.cNerds.dailyMoment.children.dto;

import java.util.Date;

import com.cNerds.dailyMoment.core.entity.EntityInfoImpl;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class ChildrenInfo extends EntityInfoImpl {
	
	private int childrenNo;
	private int familyNo;
	private String childrenName;
	private Date childrenBirth;
	private int childrenGender;
	private float weight;
	private float height;
	private String newBornBabyName;
	private String imageName;
	
	private String enChildrenNo;
	
	
	
	
	
}
