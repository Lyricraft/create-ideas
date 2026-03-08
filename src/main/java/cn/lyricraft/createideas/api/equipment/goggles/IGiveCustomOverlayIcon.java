package cn.lyricraft.createideas.api.equipment.goggles;

import com.simibubi.create.AllItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IGiveCustomOverlayIcon {
    default ItemStack getIcon(OverlayIconContext context) {
        return AllItems.GOGGLES.asStack();
    }
    record OverlayIconContext(ItemStack itemStack, boolean withGoggle, boolean isPlayerSneaking){
    }
}
