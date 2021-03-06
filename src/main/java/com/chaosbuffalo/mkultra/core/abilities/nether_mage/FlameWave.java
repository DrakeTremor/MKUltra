package com.chaosbuffalo.mkultra.core.abilities.nether_mage;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.FlameWavePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.effects.spells.SoundPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class FlameWave extends PlayerAbility {
    public static final FlameWave INSTANCE = new FlameWave();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getAbilityId()));
    }

    public static float BASE_DAMAGE = 8.0f;
    public static float DAMAGE_SCALE = 2.0f;

    private FlameWave() {
        super(MKUltra.MODID, "ability.flame_wave");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 20 - currentRank * 5;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 8 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 5.0f + 2.0f * currentRank;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_fire;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_fire_7;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND / 2 * currentRank;
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        int level = data.getAbilityRank(getAbilityId());

        // What to do for each target hit
        SpellCast flames = FlameWavePotion.Create(entity, BASE_DAMAGE, DAMAGE_SCALE);
        SpellCast particles = ParticlePotion.Create(entity,
                EnumParticleTypes.LAVA.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false,
                new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0),
                40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(flames, level, getTargetType())
                .spellCast(particles, level, getTargetType())
                .spellCast(SoundPotion.Create(entity, ModSounds.spell_fire_1, SoundCategory.PLAYERS),
                        1, getTargetType())
                .instant()
                .color(16737305).radius(getDistance(level), true)
                .particle(EnumParticleTypes.LAVA)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.LAVA.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 2.5f,
                        lookVec),
                entity, 50.0f);
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
    }
}