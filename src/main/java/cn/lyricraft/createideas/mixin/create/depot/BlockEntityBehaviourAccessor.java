package cn.lyricraft.createideas.mixin.create.depot;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour.class)
public interface BlockEntityBehaviourAccessor {
    @Accessor("blockEntity")
    SmartBlockEntity getBlockEntity();
}
