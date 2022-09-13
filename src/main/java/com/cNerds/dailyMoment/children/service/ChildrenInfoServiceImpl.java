package com.cNerds.dailyMoment.children.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cNerds.dailyMoment.children.dao.ChildrenInfoDao;
import com.cNerds.dailyMoment.children.dto.ChildrenInfo;
import com.cNerds.dailyMoment.children.dto.ChildrenInfoCriterion;
import com.cNerds.dailyMoment.core.service.GenericServiceImpl;
import com.cNerds.dailyMoment.core.util.CryptoUtil;
import com.cNerds.dailyMoment.user.dto.UserInfo;
import com.cNerds.dailyMoment.user.dto.UserInfoCriterion;

@Service
public class ChildrenInfoServiceImpl extends GenericServiceImpl<ChildrenInfo, ChildrenInfoCriterion, ChildrenInfoDao> implements ChildrenInfoService {
	
	 
	public ChildrenInfoServiceImpl() {
        super(ChildrenInfoDao.class);
    }
		

	@Override
	public List<ChildrenInfo> list(ChildrenInfoCriterion childrenCriterion) {
		List<ChildrenInfo> childrenInfoList = dao.list(childrenCriterion);
		if(!childrenInfoList.isEmpty()) {
			for(ChildrenInfo item : childrenInfoList) {
				item.setEnChildrenNo(CryptoUtil.encode(Integer.toString(item.getChildrenNo())));
			}
		}
		return childrenInfoList;
	}
	
	@Override
	public ChildrenInfo detail (ChildrenInfo childrenInfo) {
		childrenInfo = dao.detail(childrenInfo);
		childrenInfo.setEnChildrenNo(CryptoUtil.encode(Integer.toString(childrenInfo.getChildrenNo())));
		return childrenInfo;
	}


	@Override
	public int searchMaxChildrenNo() {
		return dao.searchMaxChildrenNo();
	}
	
    

}