package net.jordimp.casino;

import jakarta.annotation.PostConstruct;
import java.util.Date;
import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import net.jordimp.casino.services.PlayerServiceImpl;
import net.jordimp.casino.services.SampleJobService;
import net.jordimp.casino.utils.CasinoLoggerUtils;
import net.jordimp.casino.utils.EnvWrapperUtils;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartUpInit {
  // https://medium.com/@dmarko484/spring-boot-startup-init-through-postconstruct-765b5a5c1d29

  @Autowired private PlayerServiceImpl playerService;

  @Autowired private JobScheduler jobScheduler;

  @SuppressWarnings("unused")
  @Autowired
  private SampleJobService sampleJobService;

  @Autowired private Environment env;

  @PostConstruct
  public void init() {
    Player postPlayer = new Player(new Date(), 320L, "TEST-UUID-01", UserProvider.POKERSTAR);
    playerService.save(postPlayer);
    CasinoLoggerUtils.debug("init: inserting TEST-UUID-01 player");
    if (env != null) {
      EnvWrapperUtils.setEnv(env);
      CasinoLoggerUtils.debug("StartUpInit", String.format("Environment: %s", env));
    } else {
      CasinoLoggerUtils.debug("StartUpInit", "Environment = null");
    }
  }

  @PostConstruct
  public void scheduleRecurrently() {
    // Only schedule recurring jobs when JobRunr background server is enabled.
    // Tests disable JobRunr via `org.jobrunr.background-job-server.enabled=false` so
    // we skip scheduling to avoid JobRunr runtime errors during test startup.
    String jobrunrEnabled = env.getProperty("org.jobrunr.background-job-server.enabled", "true");
    if (!Boolean.parseBoolean(jobrunrEnabled)) {
      CasinoLoggerUtils.debug(
          "StartUpInit",
          "JobRunr scheduling disabled by property org.jobrunr.background-job-server.enabled=false");
      return;
    }
    // schedule using job name + cron + JobLambda (compatible with the JobRunr version on the
    // classpath)
    jobScheduler.scheduleRecurrently(
        "SampleJobService.executeSampleJob",
        Cron.every15minutes(),
        () -> sampleJobService.executeSampleJob("New Player and his Bets"));
  }
}
