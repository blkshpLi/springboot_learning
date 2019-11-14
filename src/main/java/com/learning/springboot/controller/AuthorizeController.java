package com.learning.springboot.controller;


import com.learning.springboot.dto.AccessToken;
import com.learning.springboot.dto.GithubUser;
import com.learning.springboot.mapper.UserMapper;
import com.learning.springboot.model.User;
import com.learning.springboot.provider.GithubProvider;
import com.learning.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.client.redirect_uri}")
    private String redirect_uri;

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserService userService;

    /**
     * Github账号授权
     * @param code
     * @param state
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessToken accessToken = new AccessToken();
        accessToken.setClient_id(clientId);
        accessToken.setClient_secret(clientSecret);
        accessToken.setCode(code);
        accessToken.setRedirect_uri(redirect_uri);
        accessToken.setState(state);
        String token = githubProvider.getAccessToken(accessToken);
        GithubUser githubUser = githubProvider.getUser(token);
        String s = request.getHeader("Referer");
        s.replace("http://localhost:8883","");
        //System.out.println(user.getLogin());  //获取github用户名
        if(githubUser != null && githubUser.getId() != null){
            User user = new User();
            String tempToken = UUID.randomUUID().toString();
            user.setToken(tempToken);
            user.setName(githubUser.getLogin());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);
            Cookie cookie = new Cookie("token",tempToken);
            cookie.setMaxAge(60*60*24);
            response.addCookie(cookie);
            return "redirect:" + s;
        }else{
            //登录失败
            return "redirect:" + s;
        }
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/logout")
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}
