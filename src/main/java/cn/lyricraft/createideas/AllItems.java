package cn.lyricraft.createideas;

import cn.lyricraft.createideas.content.items.PortableStressometerItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
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
}
