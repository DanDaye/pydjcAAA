package com.wd.pydjc.sys.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wd.pydjc.common.annotation.LogAnnotation;
import com.wd.pydjc.common.dto.Token;
import com.wd.pydjc.sys.model.User;
import com.wd.pydjc.sys.service.TokenManager;
import com.wd.pydjc.common.utils.UserUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 登陆相关接口
 * 
 * @author 
 *
 */
@Api(tags = "登陆")
@RestController
@RequestMapping
public class LoginController {

	@Autowired
	private TokenManager tokenManager;

	@LogAnnotation
	@ApiOperation(value = "web端登陆")
	@PostMapping("/sys/login")
	public void login(String username, String password) {
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		SecurityUtils.getSubject().login(usernamePasswordToken);
	}

	@LogAnnotation
	@ApiOperation(value = "Restful方式登陆,前后端分离时登录接口")
	@PostMapping("/sys/login/restful")
	public Token restfulLogin(String username, String password) {
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		SecurityUtils.getSubject().login(usernamePasswordToken);

		return tokenManager.saveToken(usernamePasswordToken);
	}

	@ApiOperation(value = "当前登录用户")
	@GetMapping("/sys/login")
	public User getLoginInfo() {
		return UserUtil.getCurrentUser();
	}

}
