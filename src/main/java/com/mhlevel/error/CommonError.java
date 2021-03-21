package com.mhlevel.error;

/**
 * @author quanbin
 * @date 2021-03-21
 */
public interface CommonError {

    int getErrCode();

    String getErrMsg();

    CommonError setErrMsg(String errMsg);
}
