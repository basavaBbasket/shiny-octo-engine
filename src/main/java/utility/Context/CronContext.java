package utility.Context;

import org.apache.log4j.Logger;
import org.junit.Assert;

public class CronContext {
    Logger logger = Logger.getLogger(CronContext.class);

    private String cronName = null;
    private String cronNameInDB = null;
    private String timeBeforeCronRun = null;
    private long epochTimeBeforeCronRun = -999;
    private String dbTimeAfterCronRun = null;
    private long dbEpochTimeAfterCronRun = -999;
    private String cronRunningEnv = "monolith";
    private boolean executeShellPlusCmd = false;

    public boolean isExecuteShellPlusCmd() {
        return executeShellPlusCmd;
    }

    public void setExecuteShellPlusCmd(boolean executeShellPlusCmd) {
        this.executeShellPlusCmd = executeShellPlusCmd;
    }

    public String getCronRunningEnv() {
        return cronRunningEnv;
    }

    public void setCronRunningEnv(String cronRunningEnv) {
        if(cronRunningEnv.equalsIgnoreCase("monolith") || cronRunningEnv.equalsIgnoreCase("containers")){
            this.cronRunningEnv = cronRunningEnv;
        }else {
            logger.error("Not a valid environment type. Allowed type monolith or containers");
        }
    }

    public String getCronName() {
        return cronName;
    }

    public void setCronName(String cronName) {
        this.cronName = cronName;
    }

    public String getCronNameInDB() {
        return cronNameInDB;
    }

    public void setCronNameInDB(String cronNameInDB) {
        this.cronNameInDB = cronNameInDB;
    }

    public String getTimeBeforeCronRun() {
        return timeBeforeCronRun;
    }

    public void setTimeBeforeCronRun(String timeBeforeCronRun) {
        this.timeBeforeCronRun = timeBeforeCronRun;
    }

    public long getEpochTimeBeforeCronRun() {
        return epochTimeBeforeCronRun;
    }

    public void setEpochTimeBeforeCronRun(long epochTimeBeforeCronRun) {
        this.epochTimeBeforeCronRun = epochTimeBeforeCronRun;
    }

    public String getDbTimeAfterCronRun() {
        if(dbTimeAfterCronRun==null){
            logger.error("No entry for cron in DB");
            Assert.fail("No entry for cron in DB");
        }
        return dbTimeAfterCronRun;
    }

    public void setDbTimeAfterCronRun(String dbTimeAfterCronRun) {
        this.dbTimeAfterCronRun = dbTimeAfterCronRun;
    }

    public long getDbEpochTimeAfterCronRun() {
        return dbEpochTimeAfterCronRun;
    }

    public void setDbEpochTimeAfterCronRun(long dbEpochTimeAfterCronRun) {
        this.dbEpochTimeAfterCronRun = dbEpochTimeAfterCronRun;
    }
}
