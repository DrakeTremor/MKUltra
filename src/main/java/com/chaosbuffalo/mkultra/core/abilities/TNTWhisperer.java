package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class TNTWhisperer extends BaseAbility {
    private static final int COUNT_PER_LEVEL = 1;

    public TNTWhisperer() {
        super(MKUltra.MODID, "ability.tnt_whisperer");
    }

    @Override
    public String getAbilityName() {
        return "TNT Whisperer";
    }

    @Override
    public String getAbilityDescription() {
        return "Creates TNT out of the nearby materials lying around.";
    }

    @Override
    public String getAbilityType() {
        return "Summon";
    }

    @Override
    public int getCooldown(int currentLevel){
        return 300;
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/tntwhisperer.png");
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public int getIconU() {
        return 180;
    }

    @Override
    public int getIconV() {
        return 0;
    }

    @Override
    public int getManaCost(int currentLevel){
        return 15 - currentLevel * 5;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());
        int count = level * COUNT_PER_LEVEL;

        ItemStack stack = new ItemStack(Blocks.TNT, count);
        ItemHandlerHelper.giveItemToPlayer(entity, stack);

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(),
                        ParticleEffects.CIRCLE_PILLAR_MOTION, 40, 10,
                        entity.posX, entity.posY + 0.05,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0,
                        lookVec),
                entity, 50.0f);
    }
}