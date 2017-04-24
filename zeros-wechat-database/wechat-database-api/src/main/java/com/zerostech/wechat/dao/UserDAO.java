package com.zerostech.wechat.dao;

import java.util.List;

import com.zerostech.wechat.model.UserDO;

public interface UserDAO {
    int insert(UserDO record);

    int insertSelective(UserDO record);

    List<UserDO> selectByParam(UserDO record);
}