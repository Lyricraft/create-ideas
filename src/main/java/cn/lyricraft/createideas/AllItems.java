package cn.lyricraft.createideas;

import cn.lyricraft.createideas.configs.syncConfig.SyncConfig;
import cn.lyricraft.createideas.content.tools.portableStressometer.PortableStressometerItem;
import com.simibubi.create.AllCreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CreateIdeas.MOD_ID);

    // Item Entries
    public static DeferredItem<PortableStressometerItem> PORTABLE_STRESSOMETER_ITEM = ITEMS.registerItem(
            "portable_stressometer",
            PortableStressometerItem::new,
            new Item.Properties().stacksTo(1)
    );

    public static void register(IEventBus modBus){
        ITEMS.register(modBus);
    }

    // 创造模式标签事件
    public static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        // 机械动力 标签
        if (event.getTabKey() == AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey()) {
            // 便携式应力表
            if (SyncConfig.PORTABLE_STRESSOMETER.get()) event.insertAfter(
                    com.simibubi.create.AllBlocks.STRESSOMETER.asStack(),
                    AllItems.PORTABLE_STRESSOMETER_ITEM.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
