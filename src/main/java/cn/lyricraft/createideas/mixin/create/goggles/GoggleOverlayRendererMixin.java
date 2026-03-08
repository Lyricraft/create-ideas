package cn.lyricraft.createideas.mixin.create.goggles;

import cn.lyricraft.createideas.CreateIdeas;
import cn.lyricraft.createideas.api.equipment.goggles.IGiveCustomOverlayIcon;
import cn.lyricraft.createideas.api.equipment.goggles.IGiveHoveringInformation;
import cn.lyricraft.createideas.configs.SyncConfig;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.api.equipment.goggles.IHaveCustomOverlayIcon;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(targets = "com.simibubi.create.content.equipment.goggles.GoggleOverlayRenderer")
public class GoggleOverlayRendererMixin {

    @Inject(method = "renderOverlay", at = @At(value = "INVOKE",
            target = "Lcom/simibubi/create/api/equipment/goggles/IHaveGoggleInformation;addToGoggleTooltip(Ljava/util/List;Z)Z",
            shift = At.Shift.BY, by = 3),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void addGivenHoverInfoToOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci,
                                                   @Local(name = "mc") Minecraft mc,
                                                   @Local(name = "item") LocalRef<ItemStack> item, // 要换物品会改指针，不能直接用原变量
                                                   @Local(name = "tooltip") List<Component> tooltip,
                                                   @Local(name = "be") BlockEntity be,
                                                   @Local(name = "isShifting") boolean isShifting,
                                                   @Local(name = "hasGoggleInformation") LocalBooleanRef hasGoggleInformation,
                                                   @Local(name = "goggleAddedInformation") LocalBooleanRef goggleAddedInformation) {
        if (mc.player == null || be == null) return;
        boolean goggleUsed = GogglesItem.isWearingGoggles(mc.player); // 是不是用了护目镜，包括装备着，和在允许的情况下拿着
        boolean goggleEffect = goggleAddedInformation.get(); // 护目镜是不是生效了，即添加了工具提示
        boolean putItem = ((be instanceof IGiveHoveringInformation) && (be instanceof IHaveCustomOverlayIcon))
                || goggleEffect; // 物品图标是不是已经设置了
        ItemStack mainHand = mc.player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = mc.player.getItemInHand((InteractionHand.OFF_HAND));
        // 提前处理 goggleUsed，因为有些 giveInformation 的工具可能根据是否用了护目镜调整行为
        if (SyncConfig.USE_GOGGLES_IN_HAND.get() && !goggleUsed)
            goggleUsed = (!mainHand.isEmpty() && mainHand.getItem() instanceof GogglesItem)
                    || (!offHand.isEmpty() && offHand.getItem() instanceof GogglesItem);
        // 现在处理主手
        // 手不空才进入
        if (!mainHand.isEmpty()){
            // 是护目镜还是其他东西？
            if (mainHand.getItem() instanceof GogglesItem){
                // idea 启用了没？是不是已经显示过护目镜信息了？有没有护目镜信息？
                if (SyncConfig.USE_GOGGLES_IN_HAND.get() && !goggleEffect && hasGoggleInformation.get()){
                    ((IHaveGoggleInformation) be).addToGoggleTooltip(tooltip, isShifting); // 毋须担心这里的类型转换问题，使用原始传进来的 goggleEffect 判断足矣
                    goggleEffect = true;
                    putItem = true;
                }
            }
            // 是其他东西，这东西给不给信息？
            else if (mainHand.getItem() instanceof IGiveHoveringInformation){
                // 让他加信息
                if (((IGiveHoveringInformation) mainHand.getItem()).addToTooltip(tooltip,
                        new IGiveHoveringInformation.InformationContext(be, InteractionHand.MAIN_HAND,
                                mainHand, offHand, goggleUsed, isShifting))) {
                    // 这是真加信息了，现在看图标是不是已经定义了，如过还没，看他想不想加图标
                    if (!putItem && mainHand.getItem() instanceof IGiveCustomOverlayIcon) {
                        ItemStack addedItem = ((IGiveCustomOverlayIcon) mainHand.getItem()).getIcon(
                                new IGiveCustomOverlayIcon.OverlayIconContext(mainHand, goggleUsed, isShifting)
                        );
                        // 是不是真加图标了？
                        if (addedItem != null && !addedItem.isEmpty()){
                            item.set(addedItem);
                            putItem = true;
                        }
                    }
                }
            }
        }
        // 然后处理副手
        // 手不空才进入
        if (!offHand.isEmpty()){
            // 是护目镜还是其他东西？
            if (offHand.getItem() instanceof GogglesItem){
                // idea 启用了没？是不是已经显示过护目镜信息了？有没有护目镜信息？
                if (SyncConfig.USE_GOGGLES_IN_HAND.get() && !goggleEffect && hasGoggleInformation.get()){
                    ((IHaveGoggleInformation) be).addToGoggleTooltip(tooltip, isShifting); // 毋须担心这里的类型转换问题，使用原始传进来的 goggleEffect 判断足矣
                    // goggleEffect = true;
                    // putItem = true;
                }

            }
            // 是其他东西，这东西给不给信息？
            else if (offHand.getItem() instanceof IGiveHoveringInformation){
                // 让他加信息
                if (((IGiveHoveringInformation) offHand.getItem()).addToTooltip(tooltip,
                        new IGiveHoveringInformation.InformationContext(be, InteractionHand.OFF_HAND,
                                offHand, mainHand, goggleUsed, isShifting))) {
                    // 这是真加信息了，现在看图标是不是已经定义了，如过还没，看他想不想加图标
                    if (!putItem && offHand.getItem() instanceof IGiveCustomOverlayIcon) {
                        ItemStack addedItem = ((IGiveCustomOverlayIcon) offHand.getItem()).getIcon(
                                new IGiveCustomOverlayIcon.OverlayIconContext(offHand, goggleUsed, isShifting)
                        );
                        // 是不是真加图标了？
                        if (addedItem != null && !addedItem.isEmpty()) {
                            item.set(addedItem);
                            // putItem = true;
                        }
                    }
                }
            }
        }
        // 如果工具条有内容了，那就改这两个变量，保证机械动力的代码不会提前返回
        if (!tooltip.isEmpty()){
            hasGoggleInformation.set(true);
            goggleAddedInformation.set(true);
        }
    }
}
