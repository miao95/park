package edu.buaa.sem.po;
// Generated 2018-5-2 23:58:39 by Hibernate Tools 4.3.1.Final

import java.util.Date;

/**
 * ReserveSpaceTime generated by hbm2java
 */
public class ReserveSpaceTime implements java.io.Serializable {

	private Long id;
	private String parkinglotNumber;
	private Date startTime;
	private Date endTime;
	private Integer spaceAmount;

	public ReserveSpaceTime() {
	}

	public ReserveSpaceTime(String parkinglotNumber, Date startTime, Date endTime, Integer spaceAmount) {
		this.parkinglotNumber = parkinglotNumber;
		this.startTime = startTime;
		this.endTime = endTime;
		this.spaceAmount = spaceAmount;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParkinglotNumber() {
		return this.parkinglotNumber;
	}

	public void setParkinglotNumber(String parkinglotNumber) {
		this.parkinglotNumber = parkinglotNumber;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getSpaceAmount() {
		return this.spaceAmount;
	}

	public void setSpaceAmount(Integer spaceAmount) {
		this.spaceAmount = spaceAmount;
	}

}