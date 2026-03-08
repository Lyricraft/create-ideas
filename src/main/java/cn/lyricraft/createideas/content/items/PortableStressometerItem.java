package cn.lyricraft.createideas.content.items;

import cn.lyricraft.createideas.AllItems;
import cn.lyricraft.createideas.CreateIdeas;
import cn.lyricraft.createideas.api.equipment.goggles.IGiveCustomOverlayIcon;
import cn.lyricraft.createideas.api.equipment.goggles.IGiveHoveringInformation;
import cn.lyricraft.createideas.configs.SyncConfig;
import cn.lyricraft.createideas.foundation.kinetics.networkStressCommunication.NetworkStressAnswerPayload;
import cn.lyricraft.createideas.foundation.kinetics.networkStressCommunication.NetworkStressAsker;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.KineticNetwork;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
            if (showStressInfo || showSpeedInfo) new LangBuilder(CreateIdeas.MOD_NAMESPACE)
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
                    if (NetworkStressAsker.ask(kineticBe)){
                        // 已回答
                        NetworkStressAnswerPayload.Answer answer = NetworkStressAsker.getLastAnswer();
                        double capacity = answer.capacity();
                        double stress = answer.stress();
                        double stressFraction = stress / (capacity == 0 ? 1 : capacity);
                        if (capacity > 0){
                            // 网络应力
                            IRotate.StressImpact.getFormattedStressText(stressFraction)
                                    .forGoggles(tooltip);
                            CreateLang.translate("gui.stressometer.capacity")
                                    .style(ChatFormatting.GRAY)
                                    .forGoggles(tooltip);

                            // 剩余应力
                            double remainingCapacity = capacity - stress;

                            LangBuilder su = CreateLang.translate("generic.unit.stress");
                            LangBuilder stressTip = CreateLang.number(remainingCapacity)
                                    .add(su)
                                    .style(IRotate.StressImpact.of(stressFraction)
                                            .getRelativeColor());

                            if (remainingCapacity != capacity)
                                stressTip.text(ChatFormatting.GRAY, " / ")
                                        .add(CreateLang.number(capacity)
                                                .add(su)
                                                .style(ChatFormatting.DARK_GRAY));

                            stressTip.forGoggles(tooltip, 1);
                        } else noTheoreticalSpeed(tooltip);
                    } else {
                        // 未回答
                        tooltip.addLast(Component.literal("测量中..." + NetworkStressAsker.getRestTime()));
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
}
