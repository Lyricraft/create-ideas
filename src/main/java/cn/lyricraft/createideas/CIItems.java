package cn.lyricraft.createideas;

import cn.lyricraft.createideas.configs.syncConfig.SyncConfig;
import cn.lyricraft.createideas.content.tools.portableStressometer.PortableStressometerItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.goggles.GogglesModel;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CIItems {

    public static void register(IEventBus modBus){}

    static {
        CreateIdeas.CREATE_REGISTRATE.setCreativeTab(null);
    }

    // 便携式应力表
    public static ItemEntry<PortableStressometerItem> PORTABLE_STRESSOMETER_ITEM = CreateIdeas.CREATE_REGISTRATE.item("portable_stressometer", PortableStressometerItem::new)
            .properties(p -> p.stacksTo(1))
            .defaultModel()
            .register();

    // 创造模式标签事件
    public static void addToCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        // 机械动力 标签
        if (event.getTabKey() == AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey()) {
            // 坚实置物台
            if (SyncConfig.SOLID_DEPOT.get()) event.insertAfter(
                    AllBlocks.WEIGHTED_EJECTOR.asStack(),
                    CIBlocks.SOLID_DEPOT.asStack(),
                    CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            // 便携式应力表
            if (SyncConfig.PORTABLE_STRESSOMETER.get()) event.insertAfter(
                    AllItems.GOGGLES.asStack(),
                    CIItems.PORTABLE_STRESSOMETER_ITEM.asStack(),
                    CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);

            // 我实在是不知道哪里已经把这些东西加到创造标签里过了，翻了所有标签都没有，就是搜得到，但又不显示来源。
            // 搞得我但凡在这里加到标签，打开创造物品栏就会崩溃，报错说东西已经加过一次了。我只好在这里把 TabVisibility 设成 PARENT_TAB_ONLY 来防止崩溃，但是又不太好。
            // 我怀疑是机械动力的注册自动加的。但是我试了很多办法都没法让它不自动加。而且我也找不到它在哪加的，加到哪了。问 AI，AI 也是的，车轱辘话说来说去，气死人。
            // 我真的很想骂人，但是不知道该骂谁，而且在这里骂人很不好，我只能哭了。
            // 我真的哭了。
        }
    }
}
