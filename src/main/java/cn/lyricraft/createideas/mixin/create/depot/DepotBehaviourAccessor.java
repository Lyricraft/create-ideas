package cn.lyricraft.createideas.mixin.create.depot;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(com.simibubi.create.content.logistics.depot.DepotBehaviour.class)
public interface DepotBehaviourAccessor {
    @Accessor("heldItem")
    TransportedItemStack getHeldItem();
}
