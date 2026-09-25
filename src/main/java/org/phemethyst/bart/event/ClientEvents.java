package org.phemethyst.bart.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.ui.BartUI;

@EventBusSubscriber(modid = Bart.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void bartUIPopup(RenderGuiEvent.Post event) {
        if (ASingularBoolean.heyDoIDrawTheUpgradesYet) {
            BartUI.popup(event.getGuiGraphics(), event.getPartialTick());
        }
    }
}
