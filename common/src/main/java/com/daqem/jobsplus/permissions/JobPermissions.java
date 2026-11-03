package com.daqem.jobsplus.permissions;

import net.minecraft.server.level.ServerPlayer;

public interface JobPermissions {

    boolean hasPermission(ServerPlayer player, String permission, int level);

}
