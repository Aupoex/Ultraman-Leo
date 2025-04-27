package com.upo.ultramanleo.network.packet;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.attachments.ModAttachments;
import com.upo.ultramanleo.capabilities.ITransformationState;
import com.upo.ultramanleo.skill.SkillZHandler;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.upo.ultramanleo.event.ForgeBusEvents;

public record ServerboundRequestSkillZPacket() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundRequestSkillZPacket> TYPE =
            new CustomPacketPayload.Type<>(UltramanLeo.id("request_skill_z"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRequestSkillZPacket> STREAM_CODEC =
            StreamCodec.unit(new ServerboundRequestSkillZPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ServerboundRequestSkillZPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ITransformationState state = player.getData(ModAttachments.TRANSFORMATION_STATE);
                SkillZHandler.startSkill(player, state);

                if (state.isTransformed() /* && !state.isUsingSkillZ() && state.getSkillZCooldown <= 0 */) {

                    ForgeBusEvents.startSkillZMovement(player, state);
                }
            }
        });
    }
}

