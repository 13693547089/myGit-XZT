package com.faujor.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faujor.dao.master.common.KvSequenceMapper;
import com.faujor.entity.common.KvSequenceDO;
import com.faujor.service.common.KvSequenceService;

@Service(value = "kvSequenceService")
public class KvSequenceServiceImpl implements KvSequenceService {
	@Autowired
	private KvSequenceMapper kvMapper;

	@Override
	public KvSequenceDO getKvSequenceByKeyName(String keyName) {
		KvSequenceDO kv = kvMapper.getKV(keyName);
		return kv;
	}

	@Override
	public int saveKvSequence(KvSequenceDO kv) {
		return kvMapper.save(kv);
	}

}
