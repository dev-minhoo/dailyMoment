package com.cNerds.dailyMoment.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cNerds.dailyMoment.core.service.GenericServiceImpl;
import com.cNerds.dailyMoment.user.UserInfo;
import com.cNerds.dailyMoment.user.UserInfoCriterion;
import com.cNerds.dailyMoment.user.dao.UserInfoDao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserInfoService extends GenericServiceImpl<UserInfo, UserInfoCriterion, UserInfoDao> implements UserDetailsService {

    //private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	UserInfo test = new UserInfo();
        return test;
        

    }
}