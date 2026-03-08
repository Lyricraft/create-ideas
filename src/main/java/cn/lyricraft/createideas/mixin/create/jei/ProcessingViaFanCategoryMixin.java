package cn.lyricraft.createideas.mixin.create.jei;

import cn.lyricraft.createideas.configs.CommonConfig;
import cn.lyricraft.createideas.Constants;
import cn.lyricraft.createideas.algorithms.RecipeAlgorithms;
import cn.lyricraft.createideas.compat.jei.CreateIdeasJei;
import cn.lyricraft.createideas.compat.jei.ToolTipHelper;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.content.materials.ExperienceNuggetItem;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.CreateLang;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProcessingViaFanCategory.class)
public abstract class ProcessingViaFanCategoryMixin<T extends Recipe<?>> extends CreateRecipeCategory<T> {

    public ProcessingViaFanCategoryMixin(Info<T> info) { super(info);}

    @Inject(method = "setRecipe", at = @At("TAIL"))
    public void addExpSlotToSatRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses, CallbackInfo ci) {
        // 只处理烟熏、熔炼、高炉配方，并检查配置
        if (!(recipe instanceof SmokingRecipe && CommonConfig.BULK_SMOKING_EXTRA_EXP.isTrue())
                && !(recipe instanceof SmeltingRecipe && CommonConfig.BULK_BLASTING_EXTRA_EXP.isTrue())
                && !(recipe instanceof BlastingRecipe && CommonConfig.BULK_BLASTING_EXTRA_EXP.isTrue())) return;
        // 防止产物本身为经验颗粒
        if ((getResultItem(recipe).getItem() instanceof ExperienceNuggetItem)) return;
        // 获取配方经验
        float recipeExp = ((AbstractCookingRecipe) recipe).getExperience();
        // 防止配方本身无经验
        if (recipeExp == 0) return;
        float expectation;
        // 计算期望
        if (recipe instanceof SmokingRecipe) {
            // 烟熏
            expectation = RecipeAlgorithms.getExpNuggetExpectationFromRecipeExp(
                    recipeExp, Constants.BULK_SMOKING_EXTRA_EXP_RATIO);
        } else if (recipe instanceof BlastingRecipe) {
            // 高炉
            expectation = RecipeAlgorithms.getExpNuggetExpectationFromRecipeExp(
                    recipeExp, Constants.BULK_BLASTING_EXTRA_EXP_RATIO);
        } else {
            // 熔炼
            expectation = RecipeAlgorithms.getExpNuggetExpectationFromRecipeExp(
                    recipeExp, Constants.BULK_SMELTING_EXTRA_EXP_RATIO);
        }
        // 计算概率
        float probability = RecipeAlgorithms.getResultProbabilityFromExpectation(expectation,
                Constants.BULK_PROCESSING_EXTRA_EXP_MAX_P);
        // 计算数量
        int count = RecipeAlgorithms.getResultCountFromExpectationAndProbability(expectation, probability);
        // 最大产物数为最大堆叠数量，只是为了防止在 JEI 中发生可能的显示异常，
        // 但在 process 的 mixin 注入处并无此判断，因为彼处可以分到多个堆叠，无需顾虑
        count = Math.min(count, com.simibubi.create.AllItems.EXP_NUGGET.asItem().getDefaultMaxStackSize());
        // 添加槽位
        // int finalCount = count; // lambda 表达式中使用的变量应为 final 或有效 final
        builder
                .addSlot(RecipeIngredientRole.OUTPUT, 160, 48)
                .setBackground(asDrawable(AllGuiTextures.JEI_CHANCE_SLOT), -1, -1)
                .addItemStack(com.simibubi.create.AllItems.EXP_NUGGET.asStack(count))
                .addRichTooltipCallback(ToolTipHelper.addRecipeExtendedByToolTip(
                        CreateIdeasJei.runtime.getJeiHelpers().getModIdHelper()))
                .addRichTooltipCallback((view, tooltip) -> {
                    tooltip.add(CreateLang.translateDirect("recipe.processing.chance",
                                    probability < 0.01 ? "<1" : (int) (probability * 100))
                            .withStyle(ChatFormatting.GOLD));
                })
                /*
                .addRichTooltipCallback((view, tooltip) -> {
                    tooltip.add(Component.literal("recipeExp: " + recipeExp + "; "
                    + "expectation: " + expectation + "; "
                    + "probability: " + probability + "; "
                    + "count: " + finalCount));
                })
                 */
        ;
    }
}
