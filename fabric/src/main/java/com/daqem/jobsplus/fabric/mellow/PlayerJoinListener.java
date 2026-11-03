package com.daqem.jobsplus.fabric.mellow;

import com.daqem.jobsplus.config.JobsPlusConfig;
import com.daqem.jobsplus.networking.s2c.ClientboundMaxJobsPacket;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class PlayerJoinListener {

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register(PlayerJoinListener::onJoin);
    }

    /**
     * When a player joins, send them the max jobs they can have for their client to know.
     */
    private static void onJoin(ServerGamePacketListenerImpl serverGamePacketListener, PacketSender packetSender, MinecraftServer minecraftServer) {
        ServerPlayer player = serverGamePacketListener.player;
        int jobs = JobsPlusConfig.maxJobsFor(player);
        NetworkManager.sendToPlayer(player, new ClientboundMaxJobsPacket(jobs));
    }

}
