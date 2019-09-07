package com.learning.springboot.controller;


import com.learning.springboot.dto.AccessToken;
import com.learning.springboot.dto.GithubUser;
import com.learning.springboot.mapper.UserMapper;
import com.learning.springboot.model.User;
import com.learning.springboot.provider.GithubProvider;
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
    private UserMapper userMapper;

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
        //System.out.println(user.getLogin());  //获取github用户名
        if(githubUser != null && githubUser.getId() != null){
            User user = new User();
            String t = UUID.randomUUID().toString();
            user.setToken(t);
            user.setName(githubUser.getLogin());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",t));
            return "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }
}
