package cn.lyricraft.createideas;

import cn.lyricraft.createideas.configs.ServerConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllConfigs {

    public static void register(ModContainer modContainer) {
        modContainer.registerConfig(Type.SERVER, ServerConfig.SPEC);
    }

    public static ModConfigSpec specFromType(ModConfig.Type type) {
        switch (type){
            case Type.SERVER -> {
                return ServerConfig.SPEC;
            }
            default -> {
                return null;
            }
        }
    }

    public static ModConfigSpec specFromType(String typeName) {
        switch (typeName){
            case "server" -> {
                return ServerConfig.SPEC;
            }
            default -> {
                return null;
            }
        }
    }

    public static ModConfigSpec.ConfigValue getConfigValueFromPath(List<String> path) {
        if (path.size() <= 1) return null;
        ModConfigSpec spec = specFromType(path.getFirst());
        if (spec == null) return null;
        ArrayList<String> innerPath = new ArrayList<String>(path);
        innerPath.removeFirst();
        return spec.getValues().get(innerPath);
    }

    public static ModConfigSpec.ConfigValue getConfigValueFromPath(String path) {
        return getConfigValueFromPath(Arrays.asList(path.split("\\.")));
    }
}