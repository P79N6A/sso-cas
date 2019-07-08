package com.unisinsight.sso.demo.controller;

import com.unisinsight.sso.demo.config.WebSecurityConfig;
import com.unisinsight.sso.demo.entity.User;
import com.unisinsight.sso.demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author yangxiaoyu
 */
@Controller
public class IndexController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/")
    public String index(@SessionAttribute(WebSecurityConfig.SESSION_KEY)String account, Model model){
        System.out.println("account:"+account);
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "index";
    }

    @PostMapping("/loginVerify")
    public String loginVerify(String username, String password, HttpSession session){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        boolean verify = loginService.verifyLogin(user);
        if (verify) {
            session.setAttribute(WebSecurityConfig.SESSION_KEY, username);
            return "index";
        } else {
            return "redirect:/login";
        }
    }

    /**
     * 跳转到默认页面
     * @param session
     * @return
     */
    @RequestMapping("/logout1")
    public String loginOut(HttpSession session){
        /**
         * 销毁当前服务的session,如果没有配置这一行代码,你会发现,点击退出之后,还需要刷新一下连接才能重新跳转回登录页。
         */
        session.invalidate();
        //这个是直接退出，走的是默认退出方式
        return "http://localhost:8080/cas/logout";
    }

    /**
     * 跳转到指定页面
     * @param session
     * @return
     */
    @RequestMapping("/logout2")
    public String loginOut2(HttpSession session){
        session.invalidate();
        //退出登录后，跳转到退成成功的页面，不走默认页面
        return "http://localhost:8080/cas/logout?service=http://localhost:8082/client";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:http://localhost:8080/cas/logout?service=http://localhost:8082/client2";
    }

}
