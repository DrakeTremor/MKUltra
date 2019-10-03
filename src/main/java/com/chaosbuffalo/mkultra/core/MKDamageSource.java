package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.effects.spells.AbilityMagicDamage;
import com.chaosbuffalo.mkultra.effects.spells.AbilityMeleeDamage;
import com.chaosbuffalo.mkultra.effects.spells.HolyDamagePotion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Created by Jacob on 7/14/2018.
 */
public class MKDamageSource extends EntityDamageSourceIndirect {

    private static String ABILITY_DMG_TYPE = "mkUltraAbility";
    private static String MOB_ABILITY_DAMAGE_TYPE = "mkUltraMobAbility";
    private static String MELEE_ABILITY_DMG_TYPE = "mkUltraMeleeAbility";

    private final ResourceLocation abilityId;
    private float modifierScaling;

    public ResourceLocation getAbilityId() {
        return abilityId;
    }

    public MKDamageSource(ResourceLocation abilityId, String damageTypeIn,
                          Entity source, @Nullable Entity indirectEntityIn) {
        super(damageTypeIn, source, indirectEntityIn);
        this.abilityId = abilityId;
        this.modifierScaling = 1.0f;
    }

    public float getModifierScaling(){
        return modifierScaling;
    }

    public MKDamageSource setModifierScaling(float value){
        modifierScaling = value;
        return this;
    }

    public boolean isIndirectMagic() {
        return abilityId.equals(AbilityMagicDamage.INDIRECT_MAGIC_DMG_ABILITY_ID);
    }

    public boolean isHolyDamage() {
        return abilityId.equals(HolyDamagePotion.HOLY_DMG_ABILITY_ID);
    }

    public boolean isMeleeAbility() {
        return abilityId.equals(AbilityMeleeDamage.INDIRECT_DMG_ABILITY_ID) || damageType.equals(MELEE_ABILITY_DMG_TYPE);
    }

    public static DamageSource causeIndirectMagicDamage(ResourceLocation abilityId, Entity source,
                                                        @Nullable Entity indirectEntityIn) {
        String damageType;
        if (indirectEntityIn instanceof EntityPlayer){
            damageType = ABILITY_DMG_TYPE;
        } else {
            damageType = MOB_ABILITY_DAMAGE_TYPE;
        }
        return new MKDamageSource(abilityId, damageType, source, indirectEntityIn)
                .setDamageBypassesArmor()
                .setMagicDamage();
    }

    public static DamageSource causeIndirectMagicDamage(ResourceLocation abilityId, Entity source,
                                                        @Nullable Entity indirectEntityIn, float modifierScaling) {
        String damageType;
        if (indirectEntityIn instanceof EntityPlayer){
            damageType = ABILITY_DMG_TYPE;
        } else {
            damageType = MOB_ABILITY_DAMAGE_TYPE;
        }
        return new MKDamageSource(abilityId, damageType, source, indirectEntityIn)
                .setModifierScaling(modifierScaling)
                .setDamageBypassesArmor()
                .setMagicDamage();
    }

    public static DamageSource causeIndirectMobMagicDamage(ResourceLocation abilityId, Entity source,
                                                           @Nullable Entity indirectEntityIn) {
        return new MKDamageSource(abilityId, MOB_ABILITY_DAMAGE_TYPE, source, indirectEntityIn)
                .setDamageBypassesArmor()
                .setMagicDamage();
    }

    public static DamageSource causeIndirectMobMagicDamage(ResourceLocation abilityId, Entity source,
                                                           @Nullable Entity indirectEntityIn, float modifierScaling) {
        return new MKDamageSource(abilityId, MOB_ABILITY_DAMAGE_TYPE, source, indirectEntityIn)
                .setModifierScaling(modifierScaling)
                .setDamageBypassesArmor()
                .setMagicDamage();
    }

    public static DamageSource fromMeleeSkill(ResourceLocation abilityId, Entity source,
                                              @Nullable Entity indirectEntityIn) {
        return new MKDamageSource(abilityId, MELEE_ABILITY_DMG_TYPE, source, indirectEntityIn);
    }

    public static DamageSource fromMeleeSkill(ResourceLocation abilityId, Entity source,
                                              @Nullable Entity indirectEntityIn, float modifierScaling){
        return new MKDamageSource(abilityId, MELEE_ABILITY_DMG_TYPE, source, indirectEntityIn)
                .setModifierScaling(modifierScaling);
    }
}
