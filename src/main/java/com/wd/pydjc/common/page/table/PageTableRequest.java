package com.wd.pydjc.common.page.table;

import java.io.Serializable;
import java.util.Map;

/**
 * 分页查询参数
 * 
 * @author zhulz
 * @version 2017-10-31
 *
 */
public class PageTableRequest implements Serializable {

	private static final long serialVersionUID = 7328071045193618467L;

	private Long monitoeTypeId;
	private Integer offset;
	private Integer limit;
	private Map<String, Object> params;

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	

	public Long getMonitoeTypeId() {
		return monitoeTypeId;
	}

	public void setMonitoeTypeId(Long monitoeTypeId) {
		this.monitoeTypeId = monitoeTypeId;
	}
}
