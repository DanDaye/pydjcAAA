package com.wd.pydjc.sys.service;

import java.util.List;

import com.wd.pydjc.sys.model.Mail;

public interface MailService {

	void save(Mail mail, List<String> toUser);
}
