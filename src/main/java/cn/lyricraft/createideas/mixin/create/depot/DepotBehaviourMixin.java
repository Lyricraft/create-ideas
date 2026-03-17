package cn.lyricraft.createideas.mixin.create.depot;

import cn.lyricraft.createideas.CIBlocks;
import cn.lyricraft.createideas.CreateIdeas;
import cn.lyricraft.createideas.configs.CommonConfig;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(com.simibubi.create.content.logistics.depot.DepotBehaviour.class)
public abstract class DepotBehaviourMixin extends BlockEntityBehaviour {
    public DepotBehaviourMixin(SmartBlockEntity be) {
        super(be);
    }

    @Inject(method = "canMergeItems", at = @At("RETURN"), cancellable = true)
    public void allowToMergeItems(CallbackInfoReturnable<Boolean> cir){
        DepotBehaviour that = (DepotBehaviour)(Object)this;
        Block block = ((BlockEntityBehaviourAccessor)that).getBlockEntity().getBlockState().getBlock();
        if (block == AllBlocks.DEPOT.get()){
            // 这是普通置物台
            if (CommonConfig.DEPOT_AUTO_MERGING.get().equals(CommonConfig.DepotType.ALL))
                cir.setReturnValue(true);
            else return;
        }
        if (block == CIBlocks.SOLID_DEPOT.get()){
            // 这是坚实置物台
            if (CommonConfig.DEPOT_AUTO_MERGING.get().equals(CommonConfig.DepotType.ALL)
                    || (CommonConfig.SOLID_DEPOT.get()
                        && CommonConfig.DEPOT_AUTO_MERGING.get().equals(CommonConfig.DepotType.SOLID_DEPOT_ONLY)))
                cir.setReturnValue(true);
            else return;
        }
    }
}
