package com.upo.ultramanleo.network.packet;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.attachments.ModAttachments;
import com.upo.ultramanleo.capabilities.ITransformationState;
import com.upo.ultramanleo.skill.SkillFireballHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundRequestFireballPacket() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundRequestFireballPacket> TYPE =
            new CustomPacketPayload.Type<>(UltramanLeo.id("request_fireball"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRequestFireballPacket> STREAM_CODEC =
            StreamCodec.unit(new ServerboundRequestFireballPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerboundRequestFireballPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ITransformationState state = player.getData(ModAttachments.TRANSFORMATION_STATE);

                if (state.isTransformed() /* && state.getFireballCooldownTicks() <= 0 */) {
                    UltramanLeo.LOGGER.debug("Received fireball request from {}", player.getName().getString());
                    SkillFireballHandler.shootFireball(player, state);
                }
            }
        });

    }
}