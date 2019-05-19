package com.faujor.dao.master.common;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.faujor.entity.common.KvSequenceDO;

public interface KvSequenceMapper {
	@Select("SELECT id, key_name as keyName, val_name as valName FROM sys_kv_sequence where key_name = #{keyName}")
	KvSequenceDO getKV(String keyName);

	@Insert("INSERT INTO sys_kv_sequence (id, key_name, val_name) values(#{id}, #{keyName}, #{valName})")
	int save(KvSequenceDO kvSequence);

	@Update("update sys_kv_sequence t set t.val_name = #{valName} where t.id = #{id}")
	int update(KvSequenceDO kv);
}
