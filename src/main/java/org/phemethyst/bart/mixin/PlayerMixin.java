package org.phemethyst.bart.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.buff.Buff;
import org.phemethyst.bart.buff.ModBuffs;
import org.phemethyst.bart.ui.PlayerScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow private boolean reducedDebugInfo;
    @Shadow @Final Inventory inventory;
    @Shadow @Final protected static EntityDataAccessor<Byte> DATA_PLAYER_MAIN_HAND;
    @Shadow private ItemStack lastItemInMainHand;
    @Shadow @Final private ItemCooldowns cooldowns;
    @Shadow protected ItemCooldowns createItemCooldowns() { throw new AssertionError(); }
    @Shadow private Optional<GlobalPos> lastDeathLocation;
    @Shadow private Collection<MutableComponent> prefixes;
    @Shadow private Collection<MutableComponent> suffixes;
    @Shadow private long lastDayTimeTick;
    @Shadow private Component displayname;
    @Shadow @Final private GameProfile gameProfile;
    @Shadow public InventoryMenu inventoryMenu;
    @Shadow public AbstractContainerMenu containerMenu;

    List<Buff> buffList = new LinkedList<>();

    protected PlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(EntityType.PLAYER, level);
        this.lastItemInMainHand = ItemStack.EMPTY;
        this.cooldowns = this.createItemCooldowns();
        this.lastDeathLocation = Optional.empty();
        this.prefixes = new LinkedList();
        this.suffixes = new LinkedList();
        this.lastDayTimeTick = -1L;
        this.displayname = null;
        this.setUUID(gameProfile.getId());
        this.gameProfile = gameProfile;
        this.inventoryMenu = new InventoryMenu(this.inventory, !level.isClientSide, (Player)(Object)this);
        this.containerMenu = this.inventoryMenu;
        this.moveTo((double)pos.getX() + (double)0.5F, (double)(pos.getY() + 1), (double)pos.getZ() + (double)0.5F, yRot, 0.0F);
        this.rotOffs = 180.0F;
    }

    public void handleEntityEvent(byte id) {
        if (id == 9) {
            this.completeUsingItem();
        } else if (id == 23) {
            this.reducedDebugInfo = false;
        } else if (id == 22) {
            this.reducedDebugInfo = true;
        } else if (id == -43 && level().isClientSide()) { // it's signed.
            if (buffList.isEmpty()) {
                Registry<Buff> buffReg = level().registryAccess().registryOrThrow(ModBuffs.BUFF_REGKEY);
                Bart.LOGGER.info("Buff registry size: {}", buffReg.size());

                for (Buff b : buffReg) {
                    if (b.getTarget() == Buff.Target.PLAYER) {
                        buffList.add(b);
                    }
                }
            }

            Buff nothing = new Buff("player",
                    "Nothing?",
                    "\"No downsides!\"",
                    "No upsides, either...",
                    "title @a title \"Nothing ever happens.\"",
                    "textures/upgrades/nothing.png");

            Buff[] bTemp = {nothing, nothing, nothing};

            if (buffList.size() >= 3) {
                for (int i = 0; i < 3; i++) {
                    Buff b = null;

                    b = buffList.get(Mth.randomBetweenInclusive(RandomSource.create(), 0, buffList.size() - 1));

                    if (i == 1 && b != bTemp[0]) {
                        bTemp[1] = b;
                        continue;
                    } else if (i == 1) {
                        i--;
                        continue;
                    }

                    if (i == 2 && b != bTemp[0] && b != bTemp[1]) {
                        bTemp[2] = b;
                        continue;
                    } else if (i == 2) {
                        i--;
                        continue;
                    }

                    bTemp[0] = b;
                }
            }

            Minecraft.getInstance().setScreen(new PlayerScreen(Component.literal("bart"), bTemp[0], bTemp[1], bTemp[2]));
        } else {
            super.handleEntityEvent(id);
        }
    }
}
