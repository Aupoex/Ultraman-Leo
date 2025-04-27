package com.upo.ultramanleo.network.packet;

import com.upo.ultramanleo.attachments.ModAttachments;
import com.upo.ultramanleo.capabilities.ITransformationState;
import com.upo.ultramanleo.event.ForgeBusEvents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.upo.ultramanleo.UltramanLeo;

public record ServerboundUltramanSkillPacket() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundRequestSkillZPacket> TYPE =
            new CustomPacketPayload.Type<>(UltramanLeo.id("request_skill_z"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRequestSkillZPacket> STREAM_CODEC =
            StreamCodec.unit(new ServerboundRequestSkillZPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ServerboundRequestSkillZPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ITransformationState state = player.getData(ModAttachments.TRANSFORMATION_STATE);
                if (state.isTransformed() /* && !state.isUsingSkillZ() && state.getSkillZCooldownTicks() <= 0 */ ) {
                    ForgeBusEvents.startSkillZMovement(player, state);
                }
            }
        });
    }
}