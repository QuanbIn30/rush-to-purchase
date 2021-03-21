package com.mhlevel.error;

/**
 * 包装器业务异常实现
 * @author quanbin
 * @date 2021-03-21
 */
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    //直接接收 EmBusinessError 的传参用于构建业务异常
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    //接收自定义的ErrorMsg的方式构造业务异常
    public BusinessException(CommonError commonError, String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
