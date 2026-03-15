package cn.lyricraft.createideas;

import cn.lyricraft.createideas.configs.ServerConfig;
import cn.lyricraft.lyricore.config.helper.ConfigHelper;
import cn.lyricraft.lyricore.config.helper.ConfigHelperRegistrar;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig.Type;



public class AllConfigs {
    private static ConfigHelperRegistrar helperRegistrar = ConfigHelper.getRegistrar(CreateIdeas.MOD_NAMESPACE);
    static {
        helperRegistrar.register(Type.SERVER, ServerConfig.SPEC);
    }

    public static void register(ModContainer modContainer) {
        modContainer.registerConfig(Type.SERVER, ServerConfig.SPEC);
    }
}