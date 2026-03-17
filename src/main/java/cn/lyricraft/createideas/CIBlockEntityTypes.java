package cn.lyricraft.createideas;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllDisplaySources;
import com.simibubi.create.AllMountedStorageTypes;
import com.simibubi.create.content.logistics.depot.DepotBlock;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import com.simibubi.create.content.logistics.depot.DepotRenderer;
import com.simibubi.create.content.logistics.depot.MountedDepotInteractionBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.simibubi.create.api.behaviour.display.DisplaySource.displaySource;
import static com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour.interactionBehaviour;
import static com.simibubi.create.api.contraption.storage.item.MountedItemStorageType.mountedItemStorage;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class CIBlockEntityTypes {
    public static final DeferredRegister BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateIdeas.MOD_ID);

    public static void register(IEventBus event) {
        BLOCK_ENTITY_TYPES.register(event);
    }

    public static final BlockEntityEntry<DepotBlockEntity> SOLID_DEPOT = CreateIdeas.CREATE_REGISTRATE
            .blockEntity("solid_depot", DepotBlockEntity::new)
            .validBlocks(CIBlocks.SOLID_DEPOT)
            .renderer(() -> DepotRenderer::new)
            .register();
}
