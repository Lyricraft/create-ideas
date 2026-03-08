package cn.lyricraft.createideas.conditions;

import cn.lyricraft.createideas.AllConfigs;
import cn.lyricraft.createideas.CreateIdeas;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.ICondition;

public record ConfigEnabledCondition(ModConfigSpec.BooleanValue config) implements ICondition {
    public static final MapCodec<ConfigEnabledCondition> CODEC =
            RecordCodecBuilder.mapCodec(inst ->
                inst.group(
                        Codec.STRING.fieldOf("path").forGetter( condition -> {
                            return String.join(".",condition.config.getPath());
                        } )
                ).apply(inst, ConfigEnabledCondition::create)
    );

    @Override
    public boolean test(ICondition.IContext context) {
        if (config == null){
            CreateIdeas.LOGGER.warn("一个数据包试图使用 create_ideas:config_enabled 条件来决定数据是否加载，" +
                    "然而其 path 参数不合法，测试结果将表现为 false。" +
                    "A datapack attempted to use the create_ideas:config_enabled condition to determine whether data should load, " +
                    "but the provided path parameter is invalid; consequently, the test result will evaluate to false.");
            return false;
        } else return config.isTrue();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    // 工厂方法：通过配置项名称创建条件实例
    public static ConfigEnabledCondition create(String path) {
        ModConfigSpec.ConfigValue config = AllConfigs.getConfigValueFromPath(path);
        if (!(config instanceof ModConfigSpec.BooleanValue)) { // 如果 config 是 null，instanceof 会直接返回 false，很安全
            CreateIdeas.LOGGER.warn("对于 create_ideas:config_enabled 条件无效的 path 参数 / "
                    + "Invalid path parameter for create_ideas:config_enabled condition: " + path);
            return new ConfigEnabledCondition(null);
        } else return new ConfigEnabledCondition((ModConfigSpec.BooleanValue) config);
    }
}
