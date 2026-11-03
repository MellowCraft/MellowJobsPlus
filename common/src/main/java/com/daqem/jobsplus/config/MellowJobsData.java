package com.daqem.jobsplus.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MellowJobsData {

    /**
     * A value sent from the current server to the client
     */
    public static int myMaxJobs = 0;

}
