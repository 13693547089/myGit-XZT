package com.faujor.entity.bam;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) 
public class CutLiaiField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String Width;
	private String title;
	private String field;
	private String fixed;
	private String event;
	private String edit;
	/**
	 * @return the width
	 */
	public String getWidth() {
		return Width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		Width = width;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}
	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}
	/**
	 * @return the fixed
	 */
	public String getFixed() {
		return fixed;
	}
	/**
	 * @param fixed the fixed to set
	 */
	public void setFixed(String fixed) {
		this.fixed = fixed;
	}
	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}
	/**
	 * @param event the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}
	/**
	 * @return the edit
	 */
	public String getEdit() {
		return edit;
	}
	/**
	 * @param edit the edit to set
	 */
	public void setEdit(String edit) {
		this.edit = edit;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CutLiaiField [Width=" + Width + ", title=" + title + ", field=" + field + ", fixed=" + fixed
				+ ", event=" + event + ", edit=" + edit + "]";
	}
	
	
}
