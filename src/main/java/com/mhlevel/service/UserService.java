package com.mhlevel.service;

import com.mhlevel.error.BusinessException;
import com.mhlevel.service.Model.UserModel;

/**
 * @author quanbin
 * @date 2021-03-21
 */
public interface UserService {

    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;
}
