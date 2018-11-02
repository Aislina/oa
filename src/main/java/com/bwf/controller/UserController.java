package com.bwf.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bwf.entity.User;
import com.bwf.service.IUserService;
import com.bwf.utils.StringUtils;

@Controller
@RequestMapping("user")
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	IUserService userService;
	@GetMapping("login")
	public String login( ){
		return "user/login";
	}
	
	@PostMapping("dologin")
	public String dologin( User user,HttpSession session){			
		logger.info("{}, {}" , user.getUsername() , user.getPassword());
		
		//密码加密
		user.setPassword ( StringUtils.md5 ( user.getPassword ( ) ) );
		logger.info("password:{}",user.getPassword());

		
		//执行登录功能
		User loginUser=userService.login( user );
			if ( loginUser == null ) {
				//登录失败
				logger.info("failed");
				//跳转到登录界面(枚举“1”表示登陆错误)
				return "redirect:/user/login?error=1";
			}
			else{
				//登录成功
				logger.info("succeed");
				
				//登录后的状态   要保存下来  所以要写入session
				session.setAttribute("user", loginUser);
				
				//跳转前先写入控制器  控制器用来渲染和分配页面
				//IndexController
				
				//跳转到首页（index页面的index方法）
				return "redirect:/index";
			}			
	}
}