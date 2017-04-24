package com.zerostech.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.zerostech.wechat.dao.UserDAO;
import com.zerostech.wechat.model.UserDO;

/**
 * DatabaseController
 * For test
 * @author hzzjb
 * @date 2017/4/24
 */
@RestController
@RequestMapping("database")
public class DatabaseController {

    @Autowired
    private UserDAO userDAO;

    @RequestMapping("test")
    @ResponseBody
    public String test() {
        UserDO param = new UserDO();
        List<UserDO> result = userDAO.selectByParam(param);
        return JSON.toJSONString(result);
    }
}
