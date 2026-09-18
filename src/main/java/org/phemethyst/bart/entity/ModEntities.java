package org.phemethyst.bart.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.entity.custom.BartEntity;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITYTYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Bart.MODID);

    public static final Supplier<EntityType<BartEntity>> BART =
            ENTITYTYPES.register("bart", () -> EntityType.Builder.of(BartEntity::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f).build("bart"));

    public static void register(IEventBus eventBus) {
        ENTITYTYPES.register(eventBus);
    }
}
