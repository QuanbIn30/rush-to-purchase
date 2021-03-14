package com.mhlevel.dao;

import com.mhlevel.dataobject.UserDO;
import com.mhlevel.dataobject.UserDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    int countByExample(UserDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    int deleteByExample(UserDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    int insert(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    int insertSelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    List<UserDO> selectByExample(UserDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    UserDO selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    int updateByExampleSelective(@Param("record") UserDO record, @Param("example") UserDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    int updateByExample(@Param("record") UserDO record, @Param("example") UserDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    int updateByPrimaryKeySelective(UserDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbggenerated Sun Mar 14 22:00:19 CST 2021
     */
    int updateByPrimaryKey(UserDO record);
}