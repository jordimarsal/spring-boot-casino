package net.jordimp.casino.services.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "bets")
@EntityListeners(AuditingEntityListener.class)
public class Bet implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String W_NO_TIME = "Player not logged or timed out";

  public static final String W_NO_GAME = "BAD Game ID";

  public static final String W_LIMIT_BET = "Bet out of bounds";

  public static final String WIN = "WIN! You have earned %s credits!!!";

  public static final String E_NULL = "NULL values in Bet";

  public static final String E_NO_FUNDS = "Balance is exhausted";

  @Id
  @Column(name = "uuid", nullable = false)
  private String betUUID;

  @Column(name = "bet_amount", nullable = false)
  private Double betAmount;

  @Column(name = "player_uuid", nullable = false)
  private String playerUUID;

  @Column(name = "game_uuid", nullable = false)
  private String gameUUID;

  @Column(name = "balance_before")
  private Double balancePlayer;

  @Column(name = "prize_amount")
  private Double prizeAmount;

  @Column(name = "comment")
  private String comment;

  @Column(name = "warning")
  private String warning;

  @Column(name = "is_win")
  private Boolean bad = false;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Timestamp createdAt;

  public Bet(Double amount, String playerUUID, String gameUUID, Double balancePlayer) {
    super();
    this.betAmount = amount;
    this.playerUUID = playerUUID;
    this.gameUUID = gameUUID;
    this.setBalancePlayer(balancePlayer);
  }

  // Required by JPA
  protected Bet() {}

  public String getBetUUID() {
    return betUUID;
  }

  public void setBetUUID(String betUUID) {
    this.betUUID = betUUID;
  }

  public Double getBetAmount() {
    return betAmount;
  }

  public void setBetAmount(Double amount) {
    this.betAmount = amount;
  }

  public String getPlayerUUID() {
    return playerUUID;
  }

  public void setPlayerUUID(String playerUUID) {
    this.playerUUID = playerUUID;
  }

  public String getGameUUID() {
    return gameUUID;
  }

  public void setGameUUID(String gameUUID) {
    this.gameUUID = gameUUID;
  }

  public Double getBalancePlayer() {
    return balancePlayer;
  }

  public void setBalancePlayer(Double balancePlayer) {
    this.balancePlayer = balancePlayer;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public Double getPrizeAmount() {
    return prizeAmount;
  }

  public void setPrizeAmount(Double prizeAmount) {
    this.prizeAmount = prizeAmount;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getWarning() {
    return warning;
  }

  public void setWarning(String warning) {
    this.warning = warning;
  }

  public boolean isBad() {
    return bad;
  }

  public void setBad(Boolean bad) {
    this.bad = bad;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  // Domain behavior
  public void calculateWin(double prizeAmount) {
    if (prizeAmount > 0) {
      this.prizeAmount = prizeAmount;
      this.bad = false;
    }
  }

  public double getNetBalanceChange() {
    double prize = prizeAmount != null ? prizeAmount : 0.0;
    return prize - betAmount;
  }

  @Override
  public String toString() {
    return "Bet [betUUID="
        + betUUID
        + ", gameUUID="
        + gameUUID
        + ", prizeAmount="
        + prizeAmount
        + ", comment="
        + comment
        + ", balancePlayer="
        + balancePlayer
        + "]";
  }

  public String constructor() {
    return "Bet [betAmount="
        + betAmount
        + ", playerUUID="
        + playerUUID
        + ", gameUUID="
        + gameUUID
        + ", balancePlayer="
        + balancePlayer
        + "]";
  }
}
