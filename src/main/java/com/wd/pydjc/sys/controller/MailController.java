package com.wd.pydjc.sys.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wd.pydjc.common.annotation.LogAnnotation;
import com.wd.pydjc.sys.dao.MailDao;
import com.wd.pydjc.sys.model.Mail;
import com.wd.pydjc.sys.model.MailTo;
import com.wd.pydjc.common.page.table.PageTableRequest;
import com.wd.pydjc.common.page.table.PageTableHandler;
import com.wd.pydjc.common.page.table.PageTableResponse;
import com.wd.pydjc.common.page.table.PageTableHandler.CountHandler;
import com.wd.pydjc.common.page.table.PageTableHandler.ListHandler;
import com.wd.pydjc.sys.service.MailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "邮件")
@RestController
@RequestMapping("/mails")
public class MailController {

	@Autowired
	private MailDao mailDao;
	@Autowired
	private MailService mailService;

	@LogAnnotation
	@PostMapping
	@ApiOperation(value = "保存邮件")
	@RequiresPermissions("mail:send")
	public Mail save(@RequestBody Mail mail) {
		String toUsers = mail.getToUsers().trim();
		if (StringUtils.isBlank(toUsers)) {
			throw new IllegalArgumentException("收件人不能为空");
		}

		toUsers = toUsers.replace(" ", "");
		toUsers = toUsers.replace("；", ";");
		String[] strings = toUsers.split(";");

		List<String> toUser = Arrays.asList(strings);
		toUser = toUser.stream().filter(u -> !StringUtils.isBlank(u)).map(u -> u.trim()).collect(Collectors.toList());
		mailService.save(mail, toUser);

		return mail;
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取邮件")
	@RequiresPermissions("mail:all:query")
	public Mail get(@PathVariable Long id) {
		return mailDao.getById(id);
	}

	@GetMapping("/{id}/to")
	@ApiOperation(value = "根据id获取邮件发送详情")
	@RequiresPermissions("mail:all:query")
	public List<MailTo> getMailTo(@PathVariable Long id) {
		return mailDao.getToUsers(id);
	}

	@GetMapping
	@ApiOperation(value = "邮件列表")
	@RequiresPermissions("mail:all:query")
	public PageTableResponse list(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return mailDao.count(request.getParams());
			}
		},new ListHandler() {

			@Override
			public List<Mail> list(PageTableRequest request) {
				return mailDao.list(request.getParams(), request.getOffset(), request.getLimit());
			}
		}).handle(request);
	}

}
