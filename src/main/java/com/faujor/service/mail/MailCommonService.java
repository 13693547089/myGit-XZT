package com.faujor.service.mail;

import java.util.List;

import com.faujor.entity.bam.AppoMate;
import com.faujor.entity.bam.AppointMail;

public interface MailCommonService {

	public boolean sendOutlookMail(AppointMail appoint, List<AppoMate> list);

}
