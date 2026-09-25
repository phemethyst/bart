package org.phemethyst.bart.event;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Minecart;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.entity.ModEntities;
import org.phemethyst.bart.entity.client.BartModel;
import org.phemethyst.bart.entity.custom.BartEntity;
import org.phemethyst.bart.item.ModItems;

import java.util.List;

@EventBusSubscriber(modid = Bart.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BartModel.LAYER_LOCATION, BartModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.BART.get(), BartEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.getItemBySlot(EquipmentSlot.FEET)
                .is(ModItems.MAID_DRESS_BOOTS.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void imagineHittingBartCouldntBeMe(AttackEntityEvent event) {
        if (!(event.getTarget() instanceof BartEntity)) {
            return;
        }

        BartEntity bart = (BartEntity) event.getTarget();

        if (bart.passive) {
            bart.passive = false;
            bart.level().broadcastEntityEvent(bart, (byte) 2);
        }
    }

    @SubscribeEvent
    public static void sudormrf(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("bart").then(
                                Commands.literal("enableDash").then(
                                        Commands.argument("bart", EntityArgument.entities())
                                            .executes(context -> {
                                                List<Entity> bart = (List<Entity>) EntityArgument.getEntities(context, "bart");

                                                for (Entity b : bart) {
                                                    if (b instanceof BartEntity) {
                                                        ((BartEntity)b).enableDash();
                                                    }
                                                }

                                                return Command.SINGLE_SUCCESS;
                                            })))
                        .then(
                                Commands.literal("enableTelefrag").then(
                                        Commands.argument("bart", EntityArgument.entities())
                                                .executes(context -> {
                                                    List<Entity> bart = (List<Entity>) EntityArgument.getEntities(context, "bart");

                                                    for (Entity b : bart) {
                                                        if (b instanceof BartEntity) {
                                                            ((BartEntity)b).enableTelefrag();
                                                        }
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                })))
                        .then(
                                Commands.literal("enableDeath").then(
                                        Commands.argument("bart", EntityArgument.entities())
                                                .executes(context -> {
                                                    List<Entity> bart = (List<Entity>) EntityArgument.getEntities(context, "bart");

                                                    for (Entity b : bart) {
                                                        if (b instanceof BartEntity) {
                                                            ((BartEntity)b).enableDeath();
                                                        }
                                                    }

                                                    return Command.SINGLE_SUCCESS;
                                                })))
                        .then(
                                Commands.literal("execute").then(
                                        Commands.argument("bart", EntityArgument.entities()).then(
                                            Commands.argument("str", StringArgumentType.string())
                                                    .executes(context -> {
                                                        String bart = StringArgumentType.getString(context, "str");

                                                        context.getSource().getServer().getCommands().performPrefixedCommand(context.getSource().withPermission(4), bart);

                                                        List<Entity> bart2 = (List<Entity>) EntityArgument.getEntities(context, "bart");

                                                        for (Entity b : bart2) {
                                                            if (b instanceof BartEntity) {
                                                                ((BartEntity)b).buffPicked();
                                                            }
                                                        }

                                                        return Command.SINGLE_SUCCESS;
                                                }))))
        );
    }
}
