package com.usian.service.Impl;

import com.usian.mapper.TbUserMapper;
import com.usian.pojo.TbUser;
import com.usian.pojo.TbUserExample;
import com.usian.redis.RedisClient;
import com.usian.service.SSOService;
import com.usian.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SSOServiceImpl implements SSOService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private RedisClient redisClient;

    @Override
    public boolean checkUserInfo(String checkValue, Integer checkFlag) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        // 1、查询条件根据参数动态生成：1、2分别代表username、phone
        if (checkFlag == 1) {
            criteria.andUsernameEqualTo(checkValue);
        } else if (checkFlag == 2) {
            criteria.andPhoneEqualTo(checkValue);
        }
        // 2、从tb_user表中查询数据
        List<TbUser> list = tbUserMapper.selectByExample(example);
        // 3、判断查询结果，如果查询到数据返回false。
        if (list == null || list.size() == 0) {
            // 4、如果没有返回true。
            return true;
        }
        // 5、如果有返回false。
        return false;
    }

    /**
     * 用户注册
     */
    @Override
    public Integer userRegister(TbUser user) {
        //将密码做加密处理。
        String pwd = MD5Utils.digest(user.getPassword());
        user.setPassword(pwd);
        //补齐数据
        Date date = new Date();
        user.setCreated(date);
        user.setUpdated(date);
        return this.tbUserMapper.insertSelective(user);
    }

    @Override
    public Map userLogin(String username, String password) {
        HashMap map = new HashMap<>();
        String pwd  = MD5Utils.digest(password);
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(pwd);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if(tbUsers !=null || tbUsers.size()>0){
            String token = UUID.randomUUID().toString();
            redisClient.set("token",tbUsers.get(0));
            //cookie 返回浏览器的信息，前端自己存入cookie；
            map.put("token",token);
            map.put("userid",tbUsers.get(0).getId());
            map.put("username",tbUsers.get(0).getUsername());
            return map;
        }
       return map;
    }

    /**
     * 查询用户登录是否过期
     * @param token
     * @return
     */
    @Override
    public TbUser getUserByToken(String token) {
        TbUser tbUser = (TbUser) redisClient.get("token");
        if(tbUser!=null){
            //需要重置key的过期时间。
            redisClient.expire("token",60*5*12);
            return tbUser;
        }
        return null;
    }

    /**
     * 用户退出登录
     * @param token
     */
    @Override
    public Boolean logOut(String token) {
        return redisClient.del("token");
    }
}
