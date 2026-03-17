package cn.lyricraft.createideas.datagen;

import cn.lyricraft.createideas.datagen.i18n.enUsLanProvider;
import cn.lyricraft.createideas.datagen.i18n.zhCnLanProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class CII18n {
    public static void createProvider(GatherDataEvent event){
        event.createProvider(enUsLanProvider::new);
        event.createProvider(zhCnLanProvider::new);
    }
}
