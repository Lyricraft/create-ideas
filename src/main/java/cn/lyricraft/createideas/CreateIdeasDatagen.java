package cn.lyricraft.createideas;

import cn.lyricraft.createideas.datagen.CII18n;
import cn.lyricraft.createideas.datagen.CIRecipeProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = CreateIdeas.MOD_ID)
public class CreateIdeasDatagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.createProvider(CIRecipeProvider::new);
        CII18n.createProvider(event);
    }
}
