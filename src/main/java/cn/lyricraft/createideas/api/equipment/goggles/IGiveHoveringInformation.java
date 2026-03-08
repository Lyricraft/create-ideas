package cn.lyricraft.createideas.api.equipment.goggles;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public interface IGiveHoveringInformation {
    default boolean addToTooltip(List<Component> tooltip, InformationContext context) {
        return false;
    }

    record InformationContext(BlockEntity blockEntity, InteractionHand hand, ItemStack itemStack, ItemStack otherHand,
                              boolean withGoggle, boolean isPlayerSneaking){
    }
}
