package cn.lyricraft.createideas;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllDisplaySources;
import com.simibubi.create.AllMountedStorageTypes;
import com.simibubi.create.content.logistics.depot.DepotBlock;
import com.simibubi.create.content.logistics.depot.MountedDepotInteractionBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.simibubi.create.api.behaviour.display.DisplaySource.displaySource;
import static com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour.interactionBehaviour;
import static com.simibubi.create.api.contraption.storage.item.MountedItemStorageType.mountedItemStorage;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class CIBlocks {

    public static void register(IEventBus event) {}

    static {
        CreateIdeas.CREATE_REGISTRATE.setCreativeTab(null);
    }

    public static final BlockEntry<DepotBlock> SOLID_DEPOT = CreateIdeas.CREATE_REGISTRATE.block("solid_depot", DepotBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .transform(axeOrPickaxe())
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .transform(displaySource(AllDisplaySources.ITEM_NAMES))
            .onRegister(interactionBehaviour(new MountedDepotInteractionBehaviour()))
            .transform(mountedItemStorage(AllMountedStorageTypes.DEPOT))
            .item()
            .transform(customItemModel("_", "block"))
            .register();
}
