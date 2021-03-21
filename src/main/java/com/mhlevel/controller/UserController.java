package com.mhlevel.controller;

import com.mhlevel.controller.viewobject.UserVO;
import com.mhlevel.error.BusinessException;
import com.mhlevel.response.CommonReturnType;
import com.mhlevel.service.Model.UserModel;
import com.mhlevel.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author quanbin
 * @date 2021-03-21
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException{
        //调用Service服务获取对应id的用户的对象并返回给客户端
        UserModel userModel = userService.getUserById(id);

        if(userModel == null) {
            userModel.setEncrptPassword("123");
//            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域模型对象转化为可以供 UI 使用的 VIEW OBJECT
        UserVO userVO = convertFromMode(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromMode(UserModel userModel){
        if(userModel == null) {
            return null;
        }
        UserVO userVO  = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }
}
