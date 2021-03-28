package com.mhlevel.error;

/**
 * @author quanbin
 * @date 2021-03-21
 */
public enum EmBusinessError implements CommonError {

    //通用错误类型1000001
    PARAMETER_ERROR(100001, "参数不合法"),
    UNKNOW_ERROR(100002, "未知错误"),

    //20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码不正确")
    ;

    private int errCode;

    private String errMsg;

    private EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return errCode;
    }

    @Override
    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
