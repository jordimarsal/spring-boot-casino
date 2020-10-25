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
import net.jordimp.casino.utils.CasinoLogger;

@Service
public class SampleJobService {

	@Autowired
	private PlayerServiceImpl playerService;
	
	@Autowired
	private GamePlayServiceImpl gamePlayService;
	
    public static final long EXECUTION_TIME = 5000L;

    private AtomicInteger count = new AtomicInteger();
    private AtomicInteger betCount = new AtomicInteger();

    @Job(name = "The sample job with variable %0", retries = 2)
    public void executeSampleJob(String variable) {

    	CasinoLogger.info("The Job has begun", variable);
    	//CasinoLogger.info("Player           ", ""+buildPlayer());
    	going();
        try {
            Thread.sleep(EXECUTION_TIME);
        } catch (InterruptedException e) {
        	CasinoLogger.error("Error while executing sample job", e);
        } finally {
            count.incrementAndGet();
            CasinoLogger.info("The Job has finished.");
        }
    }

    public int getNumberOfInvocations() {
        return count.get();
    }
    
    public int getNumberOfBet() {
        return betCount.get();
    }
    
    public void going() {
    	//Player postPlayer = new Player(new Date(), 320L, "TEST-UUID-01", UserProvider.POKERSTAR);
    	Player player = buildPlayer();
    	CasinoLogger.info("JOB: New Player  ", player.toString());
    	Double balancePlayer = (double) ThreadLocalRandom.current().nextInt(100, 200 + 1);
		playerService.save(player);
		playerService.purgeLogins();
		
		Bet retBet = null;
		for(int i=0;i<=15;i++){
			if (retBet != null){
				balancePlayer = retBet.getBalancePlayer();
			}
			Bet nextBet = buildBet(player, balancePlayer);
			if(nextBet.getBalancePlayer()-nextBet.getBetAmount()>=0) {
				CasinoLogger.info("JOB: New Bet     ", nextBet.constructor());
				retBet = gamePlayService.bet(nextBet);
			}
		}

		CasinoLogger.info("END JOB");
    }
    
    private Bet buildBet(Player player, Double balancePlayer) {
		// Bet(Double amount, String playerUUID, String gameUUID, Double balancePlayer)
    	betCount.incrementAndGet();
    	Double amount = (double) ThreadLocalRandom.current().nextInt(1, 30 + 1);
    	Bet newBet = new Bet(amount, player.getUUID(), getGameUUID(),  balancePlayer);
    	newBet.setBetUUID("BET-CRON-UUID-"+getNumberOfBet());
		return newBet;
	}

	private Player buildPlayer() {
    	long randomNum = (long) ThreadLocalRandom.current().nextInt(100, 320 + 1);
    	Player player = new Player(new Date(), randomNum, "PLAYER-CRON-UUID-"+getNumberOfInvocations(), getProvider());
    	return player;
    }
    
    private UserProvider getProvider() {
    	int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
    	String [] literals = {"POKERSTAR","BWIN","OTHER"};
    	return UserProvider.valueOf(literals[randomNum]);
    }
    
    private String getGameUUID() {
    	int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
    	String [] literals = {"VIDEOBINGO-UUID","BLACKJACK-UUID","POKER-UUID","ROULETTE-UUID","SLOT-UUID"};
    	return literals[randomNum];
    }
    

}

