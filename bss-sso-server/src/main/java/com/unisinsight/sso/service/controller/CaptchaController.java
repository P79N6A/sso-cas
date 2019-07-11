package com.unisinsight.sso.service.controller;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.unisinsight.sso.utils.CaptchaUtil;
import com.unisinsight.sso.utils.Constants;
import com.unisinsight.sso.utils.KaptchaCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码控制器
 *
 * @author yangxiaoyu
 */
@Controller
public class CaptchaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaController.class);

    /**
     * 谷歌kaptcha验证码路径
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/captcha", produces = "image/png")
    public void kaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expire", 0);
        try {
            //生产验证码字符串并保存到session中
            StringBuffer code = new StringBuffer();
            BufferedImage image = CaptchaUtil.genRandomCodeImage(code);
            request.getSession().removeAttribute(Constants.KEY_CAPTCHA);
            request.getSession().setAttribute(Constants.KEY_CAPTCHA, code.toString());
            ImageIO.write(image, "jpeg", response.getOutputStream());
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.error("生成验证码出错", e);
        }
    }


    /**
     * 用于前端ajax校验
     */
    @RequestMapping(value = "/chkCode", method = RequestMethod.POST)
    public void checkCode(@RequestParam("code") String code, HttpServletRequest req, HttpServletResponse resp) {

        //获取session中的验证码
        String storeCode = (String) req.getSession().getAttribute("captcha_code");
        code = code.trim();
        //返回值
        Map<String, Object> map = new HashMap<String, Object>();
        //验证是否对
        if (!StringUtils.isEmpty(storeCode) && code.equals(storeCode)) {
            map.put("error", false);
            map.put("msg", "验证成功");
        } else if (StringUtils.isEmpty(code)) {
            map.put("error", true);
            map.put("msg", "验证码不能为空");
        } else {
            map.put("error", true);
            map.put("msg", "验证码错误");
        }
        this.writeJSON(resp, map);
    }

    /**
     * 在SpringMvc中获取到Session
     *
     * @return
     */
    public void writeJSON(HttpServletResponse response, Object object) {
        try {
            //设定编码
            response.setCharacterEncoding("UTF-8");
            //表示是json类型的数据
            response.setContentType("application/json");
            //获取PrintWriter 往浏览器端写数据
            PrintWriter writer = response.getWriter();
            String json = JSON.toJSONString(object);
            //写数据到浏览器
            writer.write(json);
            //刷新，表示全部写完，把缓存数据都刷出去
            writer.flush();
            //关闭writer
            writer.close();
        } catch (IOException e) {
            LOGGER.error("验证码校验出错", e);
        }
    }
}
