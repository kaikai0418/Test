package com.usian.poi;


import com.usian.pojo.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient("usian-sso-service")
public interface SsoFeign {

    @RequestMapping("/user/checkUserInfo/{checkValue}/{checkFlag}")
    public boolean checkUserInfo(@PathVariable String checkValue, @PathVariable Integer checkFlag);

    @RequestMapping("/user/userRegister")
    public Integer userRegister(@RequestBody TbUser user);

    @RequestMapping("/user/userLogin")
    public Map userLogin(@RequestParam String username, @RequestParam String password);

    @PostMapping("/user/getUserByToken/{token}")
    public TbUser getUserByToken(@PathVariable String token);

    @PostMapping("/user/logOut")
    public Boolean logOut(@RequestParam String token);
}
