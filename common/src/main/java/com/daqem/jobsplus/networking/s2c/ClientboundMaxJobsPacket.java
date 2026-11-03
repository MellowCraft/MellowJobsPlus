package com.daqem.jobsplus.networking.s2c;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.toast.LevelUpJobToast;
import com.daqem.jobsplus.config.MellowJobsData;
import com.daqem.jobsplus.integration.arc.holder.holders.job.JobInstance;
import com.daqem.jobsplus.networking.JobsPlusNetworking;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ClientboundMaxJobsPacket implements CustomPacketPayload {

    private final int maxJobs;

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundMaxJobsPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClientboundMaxJobsPacket decode(RegistryFriendlyByteBuf buf) {
            return new ClientboundMaxJobsPacket(buf);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ClientboundMaxJobsPacket packet) {
            buf.writeInt(packet.maxJobs);
        }
    };

    public ClientboundMaxJobsPacket(int maxJobs) {
        this.maxJobs = maxJobs;
    }

    public ClientboundMaxJobsPacket(RegistryFriendlyByteBuf friendlyByteBuf) {
        this.maxJobs = friendlyByteBuf.readInt();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return JobsPlusNetworking.CLIENTBOUND_MAX_JOBS;
    }

    @Environment(EnvType.CLIENT)
    public static void handleClientSide(ClientboundMaxJobsPacket packet, NetworkManager.PacketContext context) {
        JobsPlus.LOGGER.info("Received Max Jobs packet from server: {}", packet.maxJobs);
        MellowJobsData.myMaxJobs = packet.maxJobs;
    }
}
