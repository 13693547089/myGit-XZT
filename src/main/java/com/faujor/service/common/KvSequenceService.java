package com.faujor.service.common;

import com.faujor.entity.common.KvSequenceDO;

public interface KvSequenceService {
	KvSequenceDO getKvSequenceByKeyName(String keyName);
	
	int saveKvSequence(KvSequenceDO kv);
}
