package com.mhlevel.service.impl;

import com.mhlevel.dao.UserInfoMapper;
import com.mhlevel.dao.UserPasswordMapper;
import com.mhlevel.dataobject.UserInfo;
import com.mhlevel.dataobject.UserPassword;
import com.mhlevel.error.BusinessException;
import com.mhlevel.error.EmBusinessError;
import com.mhlevel.service.Model.UserModel;
import com.mhlevel.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_ERROR);
        }
        if (StringUtils.isEmpty(userModel.getName())
            || userModel.getAge() == null
            || userModel.getGender() == null
            || StringUtils.isEmpty(userModel.getTelphone())){
            throw new BusinessException(EmBusinessError.PARAMETER_ERROR);
        }
        //实现model -> dataObject
        UserInfo userInfo = ConvertUserModelToUserInfo(userModel);
        try{
            userInfoMapper.insertSelective(userInfo);
        }catch (DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_ERROR, "手机号已经存在");
        }
        userModel.setId(userInfo.getId());
        UserPassword userPassword = ConvertUserModeToPassword(userModel);
        userPasswordMapper.insertSelective(userPassword);
        return;
    }

    /**
     *
     * @param telphone 是用户的手机号码
     * @param password 是加密后的密码
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @Override
    public UserModel login(String telphone, String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //通过用户的手机号码获取用户信息
        UserInfo userInfo = userInfoMapper.selectByTelphone(telphone);
        if (userInfo == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPassword userPassword = userPasswordMapper.selectByUserId(userInfo.getId());
        UserModel userModel = convertFromDataObject(userInfo, userPassword);
        if (StringUtils.equals(password, userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
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

    private static UserInfo ConvertUserModelToUserInfo(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userModel, userInfo);
        return userInfo;
    }

    private static UserPassword ConvertUserModeToPassword(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserPassword userPassword = new UserPassword();
        userPassword.setEncrptPassword(userModel.getEncrptPassword());
        userPassword.setUserId(userModel.getId());
        return userPassword;
    }

}
