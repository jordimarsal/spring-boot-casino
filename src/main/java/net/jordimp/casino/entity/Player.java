package net.jordimp.casino.entity;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "players")
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
@Component("player")
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date loginDate;

	private Long maxTime;

	@Id
	@Column(name = "uuid", nullable = false)
	private String uuid;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_provider")
	private UserProvider userProvider;

	@org.springframework.data.annotation.CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private Timestamp createdAt;

	@org.springframework.data.annotation.LastModifiedDate
	@Column(name = "updated_at")
	private Timestamp updatedAt;

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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public String toString() {
		return "Player [maxTime=" + maxTime + ", UUID=" + uuid + ", userProvider=" + userProvider + "]";
	}

}
