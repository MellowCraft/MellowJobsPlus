package com.daqem.jobsplus.fabric.permissions;

import com.daqem.jobsplus.permissions.JobPermissions;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class FabricJobPermissions implements JobPermissions {
    public static void register() {

    }

    @Override
    public boolean hasPermission(CommandSourceStack stack, String permission, int level) {
        return stack.hasPermission(level) || Permissions.check(stack, permission);
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission, int level) {
        return player.hasPermissions(level) || Permissions.check(player, permission);
    }
}
