package net.jordimp.casino;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import net.jordimp.casino.services.PlayerServiceImpl;
import net.jordimp.casino.services.SampleJobService;
import net.jordimp.casino.utils.CasinoLoggerUtils;
import net.jordimp.casino.utils.EnvWrapperUtils;

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
	
	@Autowired
	private Environment env;


	@PostConstruct
	public void init() {
		Player postPlayer = new Player(new Date(), 320L, "TEST-UUID-01", UserProvider.POKERSTAR);
		playerService.save(postPlayer);
		CasinoLoggerUtils.debug("init: inserting TEST-UUID-01 player");
		if (env != null) {
			EnvWrapperUtils.setEnv(env);
			CasinoLoggerUtils.debug("StartUpInit",String.format("Environment: %s", env));
		} else {
			CasinoLoggerUtils.debug("StartUpInit","Environment = null");
		}
	}
	
	@PostConstruct
    public void scheduleRecurrently() {
        jobScheduler.<SampleJobService>scheduleRecurrently(x -> x.executeSampleJob("New Player and his Bets"), Cron.every15minutes());
    }
}
