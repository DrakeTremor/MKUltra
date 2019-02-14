package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.spawner.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.BiFunction;
import java.util.function.Function;


@Mod.EventBusSubscriber
public class ModSpawn {

    public static final int MAX_LEVEL = 10;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItemOptions(RegistryEvent.Register<ItemOption> event) {
        ItemOption mh_test = new ItemOption(
                new ResourceLocation(MKUltra.MODID, "mh_test"),
                ItemAssigners.MAINHAND,
                new ItemChoice(new ItemStack(Items.IRON_SWORD, 1), 5, 0));
        event.getRegistry().register(mh_test);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAttributeRanges(RegistryEvent.Register<AttributeRange> event) {
        AttributeRange health_test = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "health_test"),
                BaseSpawnAttributes.MAX_HEALTH, 20.0, 50.0);
        event.getRegistry().register(health_test);
        AttributeRange set_follow = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "aggro_range"),
                BaseSpawnAttributes.FOLLOW_RANGE, 10.0, 10.0);
        event.getRegistry().register(set_follow);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobDefinitions(RegistryEvent.Register<MobDefinition> event) {

        MobDefinition test_mob =  new MobDefinition(
                new ResourceLocation(MKUltra.MODID, "test_skeleton"),
                EntitySkeleton.class, 10)
                .withAttributeRanges(
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "health_test")),
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "aggro_range"))
                        )
                .withItemOptions(
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "mh_test")))
                .withAIModifiers(
                        MKURegistry.REGISTRY_MOB_AI_MODS.getValue(
                                new ResourceLocation(MKUltra.MODID, "remove_wander")),
                        MKURegistry.REGISTRY_MOB_AI_MODS.getValue(
                                new ResourceLocation(MKUltra.MODID, "remove_watch_closest")),
                        MKURegistry.REGISTRY_MOB_AI_MODS.getValue(
                                new ResourceLocation(MKUltra.MODID, "long_range_watch_closest")
                        ))
                .withMobName("Test Skeleton");
        event.getRegistry().register(test_mob);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAIModifiers(RegistryEvent.Register<AIModifier> event) {
        AIModifier remove_wander = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_wander"),
                AIModifiers.REMOVE_AI,
                new BehaviorChoice(EntityAIWanderAvoidWater.class, 0, BehaviorChoice.TaskType.TASK));
        event.getRegistry().register(remove_wander);
        AIModifier remove_all_tasks = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_all_tasks"),
                AIModifiers.REMOVE_ALL_TASKS);
        event.getRegistry().register(remove_all_tasks);
        AIModifier remove_all_target_tasks = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_all_target_tasks"),
                AIModifiers.REMOVE_ALL_TARGET_TASKS);
        event.getRegistry().register(remove_all_target_tasks);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> getWatchClosestLongRange =
                (entity, choice) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 16.0F);
        AIModifier add_watch_closest = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "long_range_watch_closest"),
                AIModifiers.ADD_TASKS,
                new BehaviorChoice(getWatchClosestLongRange, 0, 6, BehaviorChoice.TaskType.TASK)
        );
        event.getRegistry().register(add_watch_closest);
        AIModifier remove_watch_closest = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_watch_closest"),
                AIModifiers.REMOVE_AI,
                new BehaviorChoice(EntityAIWatchClosest.class, 0, BehaviorChoice.TaskType.TASK));
        event.getRegistry().register(remove_watch_closest);
    }

}
