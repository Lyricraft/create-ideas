package cn.lyricraft.createideas.mixin.create.depot;

import cn.lyricraft.createideas.CIBlockEntityTypes;
import cn.lyricraft.createideas.CIBlocks;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.DepotBlock;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DepotBlock.class)
public abstract class DepotBlockMixin {
    @Inject(method = "getBlockEntityType", at=@At("RETURN"), cancellable = true)
    public void allowToUseSolidDepotBlockEntityType(CallbackInfoReturnable<BlockEntityType<? extends DepotBlockEntity>> cir){
        DepotBlock that = (DepotBlock)(Object)this;
        if (that == CIBlocks.SOLID_DEPOT.get()){
            cir.setReturnValue(CIBlockEntityTypes.SOLID_DEPOT.get());
        } else return;
    }
}
