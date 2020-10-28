package net.jordimp.casino.services;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.jobrunr.jobs.annotations.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import net.jordimp.casino.services.dto.Bet;
import net.jordimp.casino.utils.CasinoLoggerUtils;

@Service
public class SampleJobService {

	@Autowired
	private PlayerServiceImpl playerService;
	
	@Autowired
	private GamePlayServiceImpl gamePlayService;
	
    private static final long EXECUTION_TIME = 5000L;
    private static final int BALANCE_MIN = 100;
    private static final int BALANCE_MAX = 200;
    private static final int BET_MIN = 1;
    private static final int BET_MAX = 30;
    private static final int PLAYER_TIME_MIN = 100;
    private static final int PLAYER_TIME_MAX = 320;
    private static final int PROVIDER_IDX_MIN = 0;
    private static final int PROVIDER_IDX_MAX = 2;
    private static final int GAME_IDX_MIN = 0;
    private static final int GAME_IDX_MAX = 4;

    private AtomicInteger count = new AtomicInteger();
    private AtomicInteger betCount = new AtomicInteger();

    @Job(name = "The sample job with variable %0", retries = 2)
    public void executeSampleJob(String variable) {

    	CasinoLoggerUtils.info("The Job has begun", variable);
    	going();
        try {
            Thread.sleep(EXECUTION_TIME);
        } catch (Exception e) {
			CasinoLoggerUtils.error("Error while executing sample job", e);
        } finally {
            count.incrementAndGet();
            CasinoLoggerUtils.info("The Job has finished.");
        }
    }

    public int getNumberOfInvocations() {
        return count.get();
    }
    
    public int getNumberOfBet() {
        return betCount.get();
    }
    
    public void going() {
    	Player player = buildPlayer();
    	CasinoLoggerUtils.info("JOB: New Player  ", player.toString());
    	Double balancePlayer = (double) ThreadLocalRandom.current().nextInt(BALANCE_MIN, BALANCE_MAX + 1);
		playerService.save(player);
		playerService.purgeLogins();
		
		Bet retBet = null;
		for (int i=0; i<=15; i++) {
			if (retBet != null) {
				balancePlayer = retBet.getBalancePlayer();
			}
			Bet nextBet = buildBet(player, balancePlayer);
			if(nextBet.getBalancePlayer() - nextBet.getBetAmount()>=0) {
				CasinoLoggerUtils.info("JOB: New Bet     ", nextBet.constructor());
				retBet = gamePlayService.bet(nextBet);
			}
		}

		CasinoLoggerUtils.info("END JOB");
    }
    
    private Bet buildBet(Player player, Double balancePlayer) {
    	betCount.incrementAndGet();
    	Double amount = (double) ThreadLocalRandom.current().nextInt(BET_MIN, BET_MAX + 1);
    	Bet newBet = new Bet(amount, player.getUUID(), getGameUUID(),  balancePlayer);
		newBet.setBetUUID("BET-CRON-UUID-" + getNumberOfBet());
		return newBet;
	}

	private Player buildPlayer() {
    	long randomNum = (long) ThreadLocalRandom.current().nextInt(PLAYER_TIME_MIN, PLAYER_TIME_MAX + 1);
    	return new Player(new Date(), randomNum, "PLAYER-CRON-UUID-"+getNumberOfInvocations(), getProvider());
    }
    
    private UserProvider getProvider() {
    	int randomNum = ThreadLocalRandom.current().nextInt(PROVIDER_IDX_MIN, PROVIDER_IDX_MAX + 1);
    	String [] literals = {"POKERSTAR", "BWIN", "OTHER"};
    	return UserProvider.valueOf(literals[randomNum]);
    }
    
    private String getGameUUID() {
    	int randomNum = ThreadLocalRandom.current().nextInt(GAME_IDX_MIN, GAME_IDX_MAX + 1);
    	String [] literals = {"VIDEOBINGO-UUID", "BLACKJACK-UUID", "POKER-UUID", "ROULETTE-UUID", "SLOT-UUID"};
    	return literals[randomNum];
    }
    

}

