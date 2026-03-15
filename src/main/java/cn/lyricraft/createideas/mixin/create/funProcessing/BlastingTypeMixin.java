package cn.lyricraft.createideas.mixin.create.funProcessing;

import cn.lyricraft.createideas.configs.CommonConfig;
import cn.lyricraft.createideas.Constants;
import cn.lyricraft.createideas.algorithms.RecipeAlgorithms;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.materials.ExperienceNuggetItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;

@Mixin(AllFanProcessingTypes.BlastingType.class)
public abstract class BlastingTypeMixin implements FanProcessingType {
    @Inject(method = "process", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void addExperienceNuggetToResult(ItemStack stack, Level level, CallbackInfoReturnable<List<ItemStack>> cir,
                                            Optional<RecipeHolder<SmokingRecipe>> smokingRecipe,
                                            Optional<RecipeHolder<AbstractCookingRecipe>> smeltingRecipe) {
        List<ItemStack> result = cir.getReturnValue();
        // 检查配置中此 idea 是否启用
        if (CommonConfig.BULK_BLASTING_EXTRA_EXP.isFalse()) return;
        // 如果本身没有产物，则不改变
        if (result.isEmpty()) return;
        // 如果产物本身包含经验颗粒，则不改变
        if (result.stream().anyMatch(itemStack ->
                (itemStack.getItem() instanceof ExperienceNuggetItem)))
            return;
        // 查询原配方
        if (smeltingRecipe.isEmpty()) return;
        if (!smeltingRecipe.get().value().getType().equals(RecipeType.SMELTING) &&
                !smeltingRecipe.get().value().getType().equals(RecipeType.BLASTING))
            return; // 如果不是 smelting 或 blasting 配方则不改变
        if (smeltingRecipe.get().value().getExperience() == 0)
            return; // 如果经验值为 0 则不改变
        // 计算经验颗粒数量
        int count = RecipeAlgorithms.getResultCountFromExpectation(
                stack.getCount(),
                RecipeAlgorithms.getExpNuggetExpectationFromRecipeExp(smeltingRecipe.get().value().getExperience(),
                        smeltingRecipe.get().value().getType().equals(RecipeType.BLASTING) ?
                                Constants.BULK_BLASTING_EXTRA_EXP_RATIO : Constants.BULK_SMELTING_EXTRA_EXP_RATIO),
                Constants.BULK_PROCESSING_EXTRA_EXP_MAX_P,
                level.random.nextFloat());
        if (count > 0){
            RecipeAlgorithms.addItemStacksToResult(result, com.simibubi.create.AllItems.EXP_NUGGET, count);
            cir.setReturnValue(result);
            return; // 虽然文档里说调用 cir.setReturnValue() 就相当于返回了，但我却在前面某处发现程序还是往后执行了。
            // 用 cir.cancel 也不太妥当，所以在每处分支末尾加上了 return 。（这或许将变成我的编程习惯，如果不发生错误的话。）
        }
        return; // 无经验，不改变
    }
}
