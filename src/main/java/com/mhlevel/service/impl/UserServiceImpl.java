package com.mhlevel.service.impl;

import com.mhlevel.dao.UserInfoMapper;
import com.mhlevel.dao.UserPasswordMapper;
import com.mhlevel.dataobject.UserInfo;
import com.mhlevel.dataobject.UserPassword;
import com.mhlevel.service.Model.UserModel;
import com.mhlevel.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author quanbin
 * @date 2021-03-21
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserPasswordMapper userPasswordMapper;
    
    @Override
    public UserModel getUserById(Integer id){
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        UserPassword userPassword = userPasswordMapper.selectByUserId(id);
        if(userInfo == null){
            return null;
        }
        //通过用户的id获取用户的加密密码信息
        UserModel userModel = convertFromDataObject(userInfo, userPassword);
        return userModel;
    }

    private static UserModel convertFromDataObject(UserInfo userInfo, UserPassword userPassword){
        UserModel userModel = new UserModel();
        if(userInfo == null){
            return null;
        }
        userModel.setId(userInfo.getId());
        userModel.setName(userInfo.getName());
        userModel.setAge(userInfo.getAge());
        userModel.setGender(userInfo.getGender());
        userModel.setTelphone(userInfo.getTelphone());
        userModel.setRegisterMode(userInfo.getRegisterMode());
        userModel.setThirdPartyId(userInfo.getThirdPartyId());
//        BeanUtils.copyProperties(userInfo, userModel); //以上的设值操作可以用这个方法完成
        if(userPassword != null) {
            userModel.setEncrptPassword(userPassword.getEncrptPassword());
        }
        return userModel;
    }
}
