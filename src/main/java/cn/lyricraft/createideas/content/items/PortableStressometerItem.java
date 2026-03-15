package cn.lyricraft.createideas.content.items;

import cn.lyricraft.createideas.AllItems;
import cn.lyricraft.createideas.CreateIdeasLang;
import cn.lyricraft.createideas.api.equipment.goggles.IGiveCustomOverlayIcon;
import cn.lyricraft.createideas.api.equipment.goggles.IGiveHoveringInformation;
import cn.lyricraft.createideas.configs.CommonConfig;
import cn.lyricraft.createideas.configs.syncConfig.SyncConfig;
import cn.lyricraft.createideas.content.items.PortableStressometer.NetworkStressRequester;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity;
import com.simibubi.create.content.kinetics.gauge.StressGaugeBlockEntity;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PortableStressometerItem extends Item implements IGiveHoveringInformation, IGiveCustomOverlayIcon {

    public PortableStressometerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, InformationContext context){
        // 检查此 idea 是否启用
        if (!SyncConfig.PORTABLE_STRESSOMETER.get()) return false;
        // 以下逻辑在双手皆持时只会处理一次
        // 判断目标是否继承 KineticBlockEntity
        if((context.hand() == InteractionHand.MAIN_HAND || !(context.otherHand().getItem() instanceof PortableStressometerItem))
        && context.blockEntity() instanceof KineticBlockEntity kineticBe) {
            boolean showStressInfo = (!(context.withGoggle() && (context.blockEntity() instanceof StressGaugeBlockEntity)))
                    && IRotate.StressImpact.isEnabled();
            boolean showSpeedInfo = !(context.withGoggle() && (context.blockEntity() instanceof SpeedGaugeBlockEntity));
            if (showStressInfo || showSpeedInfo) CreateIdeasLang.builder()
                    .translate("gui.overlay.portable_gauge.info_header").forGoggles(tooltip);
            // 处理应力
            if(showStressInfo){
                CreateLang.translate("gui.stressometer.title")
                        .style(ChatFormatting.GRAY)
                        .forGoggles(tooltip);
                // 有无旋转
                if (kineticBe.getTheoreticalSpeed() == 0) noTheoreticalSpeed(tooltip);
                else {
                    // 有旋转
                    if (!NetworkStressRequester.checkBe(kineticBe) || (!NetworkStressRequester.isWaiting() && !NetworkStressRequester.haveResult())){
                        // 没想测
                        CreateIdeasLang.builder().space().translate("gui.overlay.portable_stressometer.right_click_to_measure")
                                .style(ChatFormatting.DARK_GRAY)
                                .forGoggles(tooltip);
                    } else {
                        if (!NetworkStressRequester.hasResultArrived()){
                            // 还没弄到结果
                            int percent = (int) Math.floor(((double)(System.nanoTime() - NetworkStressRequester.getStartTime()) / NetworkStressRequester.COOLDOWN) * 100);
                            percent = (Math.min(percent, 98));
                            CreateIdeasLang.builder().space().translate("gui.overlay.portable_stressometer.measuring", percent).style(ChatFormatting.DARK_GRAY)
                                    .forGoggles(tooltip);
                        } else {
                            // 弄到结果了
                            if (System.nanoTime() - NetworkStressRequester.getStartTime() < NetworkStressRequester.COOLDOWN){
                                // 还没到展示时间
                                int percent = (int) Math.floor(((double)(System.nanoTime() - NetworkStressRequester.getStartTime()) / NetworkStressRequester.COOLDOWN) * 100);
                                percent = (Math.min(percent, 99));
                                CreateIdeasLang.builder().space().translate("gui.overlay.portable_stressometer.measuring", percent).style(ChatFormatting.DARK_GRAY)
                                        .forGoggles(tooltip);
                            } else {
                                // 先看结果合不合法
                                if(NetworkStressRequester.getCapacity() < 0 || NetworkStressRequester.getStress() < 0 ||
                                        NetworkStressRequester.getCapacity() < NetworkStressRequester.getStress()){
                                    // 结果不合法
                                    CreateIdeasLang.builder().space().translate("gui.overlay.portable_stressometer.measurement_failed").style(ChatFormatting.DARK_GRAY)
                                            .forGoggles(tooltip);
                                    CreateIdeasLang.builder().space().translate("gui.overlay.portable_stressometer.right_click_to_remeasure").style(ChatFormatting.DARK_GRAY)
                                            .forGoggles(tooltip);
                                } else {
                                    // 结果合法
                                    float stressFraction = NetworkStressRequester.getStress() /
                                            (NetworkStressRequester.getCapacity() == 0 ? 1 : NetworkStressRequester.getCapacity());
                                    // 网络应力
                                    IRotate.StressImpact.getFormattedStressText(stressFraction)
                                            .forGoggles(tooltip);
                                    CreateLang.translate("gui.stressometer.capacity")
                                            .style(ChatFormatting.GRAY)
                                            .forGoggles(tooltip);

                                    // 剩余应力
                                    double remainingCapacity = NetworkStressRequester.getCapacity() - NetworkStressRequester.getStress();

                                    LangBuilder su = CreateLang.translate("generic.unit.stress");
                                    LangBuilder stressTip = CreateLang.number(remainingCapacity)
                                            .add(su)
                                            .style(IRotate.StressImpact.of(stressFraction)
                                                    .getRelativeColor());

                                    if (remainingCapacity != NetworkStressRequester.getCapacity())
                                        stressTip.text(ChatFormatting.GRAY, " / ")
                                                .add(CreateLang.number(NetworkStressRequester.getCapacity())
                                                        .add(su)
                                                        .style(ChatFormatting.DARK_GRAY));

                                    stressTip.forGoggles(tooltip, 1);
                                }
                            }
                        }
                    }
                }
            }
            // 处理转速
            if(showSpeedInfo){
                CreateLang.translate("gui.speedometer.title")
                        .style(ChatFormatting.GRAY)
                        .forGoggles(tooltip);
                IRotate.SpeedLevel.getFormattedSpeedText(kineticBe.getSpeed(), kineticBe.isOverStressed())
                        .forGoggles(tooltip);
            }
            return true;
        } else return false;
    }

    @Override
    public ItemStack getIcon(OverlayIconContext context){
        return AllItems.PORTABLE_STRESSOMETER_ITEM.toStack();
    }

    public void noTheoreticalSpeed(List<Component> tooltip){
        CreateLang.text(TooltipHelper.makeProgressBar(3, 0))
                .translate("gui.stressometer.no_rotation")
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context){
        if(!CommonConfig.PORTABLE_STRESSOMETER.get()) return InteractionResult.PASS;
        if(context.getLevel().getBlockEntity(context.getClickedPos()) instanceof KineticBlockEntity kineticBe){
            if (kineticBe.getSpeed() == 0) return InteractionResult.SUCCESS_NO_ITEM_USED;
            NetworkStressRequester.start();
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        } else return InteractionResult.PASS;
    }
}
