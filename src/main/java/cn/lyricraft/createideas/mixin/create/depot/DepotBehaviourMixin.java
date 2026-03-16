package cn.lyricraft.createideas.mixin.create.depot;

import cn.lyricraft.createideas.configs.CommonConfig;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.simibubi.create.content.logistics.depot.DepotBehaviour")
public abstract class DepotBehaviourMixin extends BlockEntityBehaviour {
    public DepotBehaviourMixin(SmartBlockEntity be) {
        super(be);
    }

    @Inject(method = "canMergeItems", at = @At("RETURN"), cancellable = true)
    public void allowToMergeItems(CallbackInfoReturnable<Boolean> cir){
        if (false) { // Todo: 条件写成 这是坚实置物台
            // 这是坚实置物台
            if (CommonConfig.DEPOT_AUTO_MERGING.get().equals(CommonConfig.DepotType.ALL)
                    || CommonConfig.DEPOT_AUTO_MERGING.get().equals(CommonConfig.DepotType.SOLID_DEPOT_ONLY))
                cir.setReturnValue(true);
            else return;
        } else {
            // 这是普通置物台
            if (CommonConfig.DEPOT_AUTO_MERGING.get().equals(CommonConfig.DepotType.ALL))
                cir.setReturnValue(true);
            else return;
        }
    }
}
