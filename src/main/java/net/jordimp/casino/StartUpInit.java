package net.jordimp.casino;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import net.jordimp.casino.services.PlayerServiceImpl;
import net.jordimp.casino.services.SampleJobService;
import net.jordimp.casino.utils.CasinoLogger;

@Component
public class StartUpInit {
	// https://medium.com/@dmarko484/spring-boot-startup-init-through-postconstruct-765b5a5c1d29

	@Autowired
	private PlayerServiceImpl playerService;
	
	@Autowired
	private JobScheduler jobScheduler;
	 
	@SuppressWarnings("unused")
	@Autowired
	private SampleJobService sampleJobService;


	@PostConstruct
	public void init() {
		Player postPlayer = new Player(new Date(), 320L, "TEST-UUID-01", UserProvider.POKERSTAR);
		playerService.save(postPlayer);
		CasinoLogger.debug("init: inserting TEST-UUID-01 player");
	}
	
	@PostConstruct
    public void scheduleRecurrently() {
        jobScheduler.<SampleJobService>scheduleRecurrently(x -> x.executeSampleJob("New Player and his Bets"), Cron.minutely());
    }
}
