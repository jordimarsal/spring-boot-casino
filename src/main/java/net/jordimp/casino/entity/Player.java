package net.jordimp.casino.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

@Component("player")
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date loginDate;

	private Long maxTime;

	private String uuid;

	@Enumerated(EnumType.STRING)
	private UserProvider userProvider;

	public Player() {
	}

	public Player(Date loginDate, Long maxTime, String uUID, UserProvider userProvider) {
		super();
		this.loginDate = loginDate;
		this.maxTime = maxTime;
		uuid = uUID;
		this.userProvider = userProvider;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Long maxTime) {
		this.maxTime = maxTime;
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uUID) {
		uuid = uUID;
	}

	public UserProvider getUserProvider() {
		return userProvider;
	}

	public void setUserProvider(UserProvider userProvider) {
		this.userProvider = userProvider;
	}

	@Override
	public String toString() {
		return "Player [maxTime=" + maxTime + ", UUID=" + uuid + ", userProvider=" + userProvider + "]";
	}

}
