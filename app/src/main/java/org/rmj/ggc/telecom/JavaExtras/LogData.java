package org.rmj.ggc.telecom.JavaExtras;

public class LogData {

    String LogDate;
    String LogUser;
    String LogTime;
    String LogTransNox;
    String LogStatus;

    public LogData(String logDate, String logUser, String logTime, String logTransNox, String logStatus) {
        LogDate = logDate;
        LogUser = logUser;
        LogTime = logTime;
        LogTransNox = logTransNox;
        LogStatus = logStatus;
    }

    public String getLogDate() {
        return LogDate;
    }

    public String getLogUser() {
        return LogUser;
    }

    public String getLogTime() {
        return LogTime;
    }

    public String getLogTransNox() {
        return LogTransNox;
    }

    public String getLogStatus() {
        return LogStatus;
    }
}
