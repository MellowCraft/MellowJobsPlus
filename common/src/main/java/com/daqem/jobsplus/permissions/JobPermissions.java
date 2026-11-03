package com.daqem.jobsplus.permissions;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public interface JobPermissions {

    boolean hasPermission(CommandSourceStack player, String permission, int level);

    boolean hasPermission(ServerPlayer player, String permission, int level);

}
