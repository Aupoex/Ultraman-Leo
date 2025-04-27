package com.upo.ultramanleo.event;

import com.upo.ultramanleo.UltramanLeo;
import com.upo.ultramanleo.client.keybinding.ModKeyBindings;
import com.upo.ultramanleo.network.packet.ServerboundRequestFireballPacket;
import com.upo.ultramanleo.network.packet.ServerboundRequestSkillZPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = UltramanLeo.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ClientForgeBusEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ModKeyBindings.ULTRAMAN_Z_KEY.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            PacketDistributor.sendToServer(new ServerboundRequestSkillZPacket());
        }
        if (ModKeyBindings.ULTRAMAN_X_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new ServerboundRequestFireballPacket());
        }
    }
}