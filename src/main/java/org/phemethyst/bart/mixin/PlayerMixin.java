package org.phemethyst.bart.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.phemethyst.bart.Bart;
import org.phemethyst.bart.buff.Buff;
import org.phemethyst.bart.buff.ModAttachments;
import org.phemethyst.bart.buff.ModBuffs;
import org.phemethyst.bart.packets.MixinBullshit;
import org.phemethyst.bart.ui.PlayerScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements MixinBullshit {
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

    public List<Buff> buffList = new LinkedList<>();
    private boolean openBuffScreen = false;

    Buff nothing = level().registryAccess().registryOrThrow(ModBuffs.BUFF_REGKEY).get(ResourceLocation.fromNamespaceAndPath("bart", "nothing"));

    private static final EntityDataAccessor<Buff.ListRecord> BUFFS =
            SynchedEntityData.defineId(
                    Player.class,
                    ModAttachments.ENTITY_SERIALIZER
            );

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineSyncedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        Buff.ListRecord output = new Buff.ListRecord(new LinkedList<>());
        output.buffs().add(nothing);
        output.buffs().add(nothing);
        output.buffs().add(nothing);
        builder.define(BUFFS, output);
    }

    Buff[] bTemp = {nothing, nothing, nothing};

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
        } else if (id == 43) { // it's signed.
            Bart.LOGGER.info("meow");

            if (!level().isClientSide()) {
                serverBuffStuff();
            } else {
                openBuffScreen = true;
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bart$clientTick(CallbackInfo ci) {
        if (!level().isClientSide || !openBuffScreen) {
            return;
        }
    }

    public void serverBuffStuff() {
        if (level().isClientSide()) {
            return;
        }

        buffList = new LinkedList<>(this.getData(ModAttachments.PLAYER_BUFFS));

        if (buffList.isEmpty() && ((Player)(Object)this).getInventory().isEmpty()) {
            Registry<Buff> buffReg = level().registryAccess().registryOrThrow(ModBuffs.BUFF_REGKEY);

            for (Buff b : buffReg) {
                if (b.getTarget() == Buff.Target.PLAYER && !Objects.equals(b.getName(), "Nothing?")) {
                    buffList.add(b);
                }
            }
        }

        List<Buff> available = new LinkedList<>(buffList);

        Buff.ListRecord bTemp = new Buff.ListRecord(new LinkedList<>());
        bTemp.buffs().add(nothing);
        bTemp.buffs().add(nothing);
        bTemp.buffs().add(nothing);

        RandomSource random = RandomSource.create();

        available.removeAll(Collections.singletonList(nothing));

        for (int i = 0; i < 3 && !available.isEmpty(); i++) {
            int index = random.nextInt(available.size());
            bTemp.buffs().set(i, available.remove(index));
        }

        this.entityData.set(BUFFS, bTemp);

        // send packet to client
        PacketDistributor.sendToAllPlayers(bTemp);
    }

    public void clientBuffStuff(Buff[] bT) {
        if (!level().isClientSide()) {
            return;
        }

        // bT is the stuff we get from packet.

        if (bT == null || bT.length != 3 || bT[0] == null || bT[1] == null || bT[2] == null) {
            return;
        }

        Minecraft.getInstance().setScreen(
                new PlayerScreen(Component.literal("bart"), bT[0], bT[1], bT[2]));
    }

    public List<Buff> getBuffs() {
        return buffList;
    }

    public void setBuffs(List<Buff> b) {
        buffList = new LinkedList<>(b);
        ((Player)(Object)this).setData(ModAttachments.PLAYER_BUFFS, buffList);
    }
}
