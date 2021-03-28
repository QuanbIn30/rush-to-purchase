package com.mhlevel.controller;

import com.alibaba.druid.util.StringUtils;
import com.mhlevel.controller.viewobject.UserVO;
import com.mhlevel.error.BusinessException;
import com.mhlevel.error.EmBusinessError;
import com.mhlevel.response.CommonReturnType;
import com.mhlevel.service.Model.UserModel;
import com.mhlevel.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author quanbin
 * @date 2021-03-21
 */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials="true", allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 获取短信验证码接口
     * @param id
     * @return
     * @throws BusinessException
     */
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



    /**
     * 用户获取OTP短信接口
     * @param telphone
     * @return
     */
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

    /**
     * 用户注册接口
     * @param telphone
     * @param otpCode
     * @param name
     * @param gender
     * @param age
     * @param password
     * @return
     * @throws BusinessException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping(value="/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "gender") Integer gender,
                                     @RequestParam(name = "age") Integer age,
                                     @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //1.验证手机号和对应的otpCode相符合
        //TODO 怎么解决跨域问题呢
//        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute("telphone");
        String inSessionOtpCode = this.httpServletRequest.getParameter("otpCode");
        if(!StringUtils.equals(otpCode, inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_ERROR,"短信验证码不符合");
        }
        //2.用户的注册流程
        UserModel userModel = new UserModel();
        userModel.setTelphone(telphone);
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setEncrptPassword(this.EnCodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    /**
     * 用户登入接口
     * @param telphone
     * @param password
     * @return
     */
    @RequestMapping(value="/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telphone") String telphone,
                                  @RequestParam(name="password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //1.入参校验
        if (org.apache.commons.lang3.StringUtils.isEmpty(telphone)
            || org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_ERROR);
        }

        //2.调用用户登入服务
        UserModel userModel = userService.login(telphone, this.EnCodeByMd5(password));

        //3.将登入凭证加入到用户登入成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);

        return CommonReturnType.create(null);
    }



    /**
     * 对象转换
     * @param userModel
     * @return
     */
    private UserVO convertFromMode(UserModel userModel){
        if(userModel == null) {
            return null;
        }
        UserVO userVO  = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

    /**
     * 将密码转码
     * @param str
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public String EnCodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }
}
