package cn.lyricraft.createideas.mixin.create.funProcessing;

import cn.lyricraft.createideas.configs.ServerConfig;
import cn.lyricraft.createideas.Constants;
import cn.lyricraft.createideas.algorithms.RecipeAlgorithms;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.materials.ExperienceNuggetItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(AllFanProcessingTypes.SmokingType.class)
public abstract class SmokingTypeMixin implements FanProcessingType {
    @Inject(method = "process", at = @At("RETURN"), cancellable = true)
    public void addExperienceNuggetToResult(ItemStack stack, Level level, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> result = cir.getReturnValue();
        // 检查配置中此 idea 是否启用
        if (ServerConfig.BULK_SMOKING_EXTRA_EXP.isFalse()) return;
        // 如果本身没有产物，则不改变
        if (result.isEmpty()) return;
        // 如果产物本身包含经验颗粒，则不改变
        if (result.stream().anyMatch(itemStack ->
                (itemStack.getItem() instanceof ExperienceNuggetItem)))
            return;
        // 查询原配方
        // 这里直接就是 SmokingRecipe 的 Optional 对象，而非和 BlastingTypeMixin 一样是 RecipeHolder 的 Optional 对象，
        // 我把它称为 RealSmokingRecipe ，即 rSmokingRecipe ，用以区分。
        Optional<SmokingRecipe> rSmokingRecipe = level.getRecipeManager()
                .getRecipeFor(RecipeType.SMOKING, new SingleRecipeInput(stack), level)
                .filter(AllRecipeTypes.CAN_BE_AUTOMATED)
                .map(RecipeHolder::value);
        if (rSmokingRecipe.isEmpty()) return;
        if (!rSmokingRecipe.get().getType().equals(RecipeType.SMOKING)) return; // 如果不是 smoking 配方则不改变
        if (rSmokingRecipe.get().getExperience() == 0)
            return; // 如果经验值为 0 则不改变
        // 计算经验颗粒数量
        int count = RecipeAlgorithms.getResultCountFromExpectation(
                stack.getCount(),
                RecipeAlgorithms.getExpNuggetExpectationFromRecipeExp(rSmokingRecipe.get().getExperience(),
                        Constants.BULK_SMOKING_EXTRA_EXP_RATIO),
                Constants.BULK_PROCESSING_EXTRA_EXP_MAX_P,
                level.random.nextFloat());
        if (count > 0){
            RecipeAlgorithms.addItemStacksToResult(result, com.simibubi.create.AllItems.EXP_NUGGET, count);
            cir.setReturnValue(result);
            return;
        }
        return; // 无经验，不改变
    }
}
