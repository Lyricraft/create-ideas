package cn.lyricraft.createideas;

import cn.lyricraft.createideas.conditions.ConfigEnabledCondition;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AllConditions {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, CreateIdeas.MOD_ID);

    public static void register(IEventBus eventBus){
        CONDITION_CODECS.register(eventBus);
    }

    public static final Supplier<MapCodec<ConfigEnabledCondition>> CONFIG_ENABLED =
            CONDITION_CODECS.register("config_enabled", () -> ConfigEnabledCondition.CODEC);
}
