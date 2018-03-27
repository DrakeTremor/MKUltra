package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.InstantIndirectMagicDamagePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class KPDarkWail extends BaseAbility {

    public static float BASE_DAMAGE = 2.0f;
    public static float DAMAGE_SCALE = 1.0f;
    public static int DURATION_BASE = 4;
    public static int DURATION_SCALE = 2;

    public KPDarkWail() {
        super(MKUltra.MODID, "ability.kp_dark_wail");
    }

    @Override
    public String getAbilityName() {
        return "KP's Dark Wail";
    }

    @Override
    public String getAbilityDescription() {
        return "Damages and roots all enemies around you.";
    }

    @Override
    public String getAbilityType() {
        return "AOE";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/kpdarkwail.png");
    }


    @Override
    public int getIconU() {
        return 36;
    }

    @Override
    public int getIconV() {
        return 36;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 16 - 2 * currentLevel;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 6 + currentLevel * 2;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 2.0f + currentLevel * 2.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        // What to do for each target hit
        SpellCast damage = InstantIndirectMagicDamagePotion.Create(entity, BASE_DAMAGE, DAMAGE_SCALE);
        SpellCast particle = ParticlePotion.Create(entity,
                EnumParticleTypes.NOTE.getParticleID(),
                ParticleEffects.CIRCLE_PILLAR_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        PotionEffect slow = new PotionEffect(MobEffects.SLOWNESS,
                DURATION_BASE + DURATION_SCALE * level * GameConstants.TICKS_PER_SECOND, 100);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(damage, level, getTargetType())
                .spellCast(particle, level, getTargetType())
                .effect(slow, getTargetType())
                .instant()
                .color(16711935).radius(getDistance(level), true)
                .particle(EnumParticleTypes.NOTE)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.NOTE.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 20, 0,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 2.0,
                        lookVec),
                entity, 50.0f);
    }
}