package com.upo.ultramanleo.network;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.network.packet.ServerboundRequestFireballPacket;
import com.upo.ultramanleo.network.packet.ServerboundRequestSkillZPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(UltramanLeo.MODID)
                .versioned(PROTOCOL_VERSION);

        registrar.playToServer(
                ServerboundRequestSkillZPacket.TYPE,
                ServerboundRequestSkillZPacket.STREAM_CODEC,
                ServerboundRequestSkillZPacket::handle
        );

        registrar.playToServer(
                ServerboundRequestFireballPacket.TYPE,
                ServerboundRequestFireballPacket.STREAM_CODEC,
                ServerboundRequestFireballPacket::handle
        );
    }
}