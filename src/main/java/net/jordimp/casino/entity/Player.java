package net.jordimp.casino.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

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

  public Player() {}

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

  public UserProvider getUserProvider() {
    return userProvider;
  }

  public void setUserProvider(UserProvider userProvider) {
    this.userProvider = userProvider;
  }

  @JsonProperty("uuid")
  public String getUUID() {
    return uuid;
  }

  // Alias for JPA/Hibernate which expects getUuid()
  public String getUuid() {
    return getUUID();
  }

  public void setUUID(String uUID) {
    uuid = uUID;
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

  // Domain behavior
  public boolean canPlaceBet(double amount) {
    return !isSessionExpired() && hasSufficientBalance(amount);
  }

  private boolean isSessionExpired() {
    if (loginDate == null || maxTime == null) {
      return true;
    }
    long elapsed = System.currentTimeMillis() - loginDate.getTime();
    return elapsed > maxTime;
  }

  private boolean hasSufficientBalance(double amount) {
    // Balance tracking will be added later
    // For now, always return true
    return true;
  }

  @Override
  public String toString() {
    return "Player [maxTime=" + maxTime + ", UUID=" + uuid + ", userProvider=" + userProvider + "]";
  }
}
