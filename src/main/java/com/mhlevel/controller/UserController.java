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

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @author quanbin
 * @date 2021-03-21
 */
@Controller
@RequestMapping("/user")
@CrossOrigin
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

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

    //用户获取OTP短信接口
    @RequestMapping(value="/getotp", method={RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone") String telphone){
        //1.需要按照一定的规则生成otp验证码
        Random random = new Random();
        int randomId = random.nextInt(99999);
        randomId += 10000;
        String otpCode = String.valueOf(randomId);
        //2.将otp验证码同对应用户的手机号关联起来，使用httpSession
        httpServletRequest.getSession().setAttribute(telphone, otpCode);

        //3.将otp验证码通过短信通道发送给用户（此处省略）
        System.out.println(" telphone : " + telphone + " otpCode : " + otpCode);
        return CommonReturnType.create(null);
    }
}
