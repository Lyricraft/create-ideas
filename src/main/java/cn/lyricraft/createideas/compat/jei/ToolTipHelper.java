package cn.lyricraft.createideas.compat.jei;

import cn.lyricraft.createideas.CreateIdeas;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.helpers.IModIdHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public final class ToolTipHelper {
    // 给 JEI 配方输出槽添加“配方扩展自”标签
    public static IRecipeSlotRichTooltipCallback addRecipeExtendedByToolTip(IModIdHelper modIdHelper) {
        return (view, tooltip) -> {
            if (modIdHelper.isDisplayingModNameEnabled())
                tooltip.add(Component.translatable("create_ideas.jei.tooltip.recipe.extendedBy",
                                modIdHelper.getFormattedModNameForModId(CreateIdeas.MOD_NAME))
                        .withStyle(ChatFormatting.GRAY));
        };
    }
}