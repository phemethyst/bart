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
import java.util.Optional;

@Mixin(Player.class)
public class PlayerMixin extends LivingEntity {
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
        Bart.LOGGER.info("entity event " + id);
        if (id == 9) {
            this.completeUsingItem();
        } else if (id == 23) {
            this.reducedDebugInfo = false;
        } else if (id == 22) {
            this.reducedDebugInfo = true;
        } else if (id == -43 && level().isClientSide()) { // it's signed.
            Registry<Buff> buffReg = level().registryAccess().registryOrThrow(ModBuffs.BUFF_REGKEY);

            Bart.LOGGER.info("meow");

            Buff b1 = null;
            Buff b2 = null;
            Buff b3 = null;

            for (Buff b : buffReg) {
                if (b.getTarget() == Buff.Target.PLAYER) {
                    if (b1 == null) {
                        b1 = b;
                        continue;
                    }

                    if (b2 == null) {
                        b2 = b;
                        continue;
                    }

                    if (b3 == null) {
                        b3 = b;
                        continue;
                    }
                }
            }

            Minecraft.getInstance().setScreen(new PlayerScreen(Component.literal("bart"), b1, b2, b3));
        } else {
            super.handleEntityEvent(id);
        }
    }

    public Iterable<ItemStack> getArmorSlots() {
        return this.inventory.armor;
    }

    public ItemStack getItemBySlot(EquipmentSlot slot1) {
        if (slot1 == EquipmentSlot.MAINHAND) {
            return this.inventory.getSelected();
        } else if (slot1 == EquipmentSlot.OFFHAND) {
            return (ItemStack)this.inventory.offhand.get(0);
        } else {
            return slot1.getType() == EquipmentSlot.Type.HUMANOID_ARMOR ? (ItemStack)this.inventory.armor.get(slot1.getIndex()) : ItemStack.EMPTY;
        }
    }

    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        this.verifyEquippedItem(stack);
        if (slot == EquipmentSlot.MAINHAND) {
            this.onEquipItem(slot, (ItemStack)this.inventory.items.set(this.inventory.selected, stack), stack);
        } else if (slot == EquipmentSlot.OFFHAND) {
            this.onEquipItem(slot, (ItemStack)this.inventory.offhand.set(0, stack), stack);
        } else if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
            this.onEquipItem(slot, (ItemStack)this.inventory.armor.set(slot.getIndex(), stack), stack);
        }
    }

    public HumanoidArm getMainArm() {
        return (Byte)this.entityData.get(DATA_PLAYER_MAIN_HAND) == 0 ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }
}
