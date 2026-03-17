package cn.lyricraft.createideas;

import cn.lyricraft.createideas.configs.CommonConfig;
import cn.lyricraft.lyricore.config.helper.ConfigHelper;
import cn.lyricraft.lyricore.config.helper.ConfigHelperRegistrar;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig.Type;



public class CIConfigs {
    private static ConfigHelperRegistrar helperRegistrar = ConfigHelper.getRegistrar(CreateIdeas.MOD_NAMESPACE);
    static {
        helperRegistrar.register(Type.COMMON, CommonConfig.SPEC);
    }

    public static void register(ModContainer modContainer) {
        modContainer.registerConfig(Type.COMMON, CommonConfig.SPEC);
    }
}