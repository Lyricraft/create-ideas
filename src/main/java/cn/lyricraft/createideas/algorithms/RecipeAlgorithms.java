package cn.lyricraft.createideas.algorithms;

import cn.lyricraft.createideas.Constants;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class RecipeAlgorithms {

    // 给定单一物品数量、产出概率、每次产出的数量和一随机值，用逆变换算法得出实际产出产物数量
    public static int getOutputCountFromProbability(int inputCount, float resultProbability, int resultCount,
                                                    float randomValue) {
        if (inputCount == 0 || resultProbability == 0.00f || resultCount == 0) return 0;
        if (resultProbability == 1.00f) return inputCount * resultCount;
        if (inputCount == 1) return randomValue < resultProbability ? resultCount : 0;
        double f = 0.0; // 积累概率
        int k = 0; // 成功次数
        double currentP = Math.pow(1.0 - resultProbability, inputCount);
        while (k <= inputCount) {
            f += currentP;
            // 若 randomValue 落入累积概率区间，确定成功次数为 k
            if (randomValue < f) break;
            k++; // 尝试下一个 k
            if (k > inputCount) break;
            // 递推计算 currentP
            currentP = currentP * ((inputCount - k + 1) * resultProbability) / (k * (1.0 - resultProbability));
        }
        return k * resultCount;
    }

    // 给定物品数量、产物数量期望、单次产出最高概率和一随机值，模拟实际产物数量
    public static int getResultCountFromExpectation (int inputCount, float resultExpectation,
                                                     float resultMaxProbability, float randomValue) {
        float resultProbability = getResultProbabilityFromExpectation(resultExpectation, resultMaxProbability);
        return getOutputCountFromProbability(inputCount, resultProbability,
                getResultCountFromExpectationAndProbability(resultExpectation, resultProbability), randomValue);
        /*
        if (resultExpectation <= resultMaxProbability)
            return getResultCountFromProbability(inputCount, resultExpectation, 1, randomValue);
        else return getResultCountFromProbability(inputCount, resultMaxProbability,
                (int) Math.floor(resultExpectation / resultMaxProbability), randomValue);
         */
    }

    public static float getResultProbabilityFromExpectation (float resultExpectation, float resultMaxProbability) {
        return Math.min(resultExpectation, resultMaxProbability);
    }

    public static int getResultCountFromExpectationAndProbability (
            float resultExpectation ,float resultProbability) {
        if (resultExpectation == resultProbability) return 1;
        else return Math.max((int) Math.floor(resultExpectation / resultProbability), 1);
    }

    // 指定配方经验，返还倍率，得到经验颗粒返还期望
    public static float getExpNuggetExpectationFromRecipeExp (float recipeExpValue, float ratio) {
        return recipeExpValue * ratio / (float) Constants.CREATE_EXP_NUGGET_VALUE;
    }

    // 将多余结果物品附加到结果尾部，自动处理物品最大堆叠数量以分散到多个堆叠
    public static void addItemStacksToResult(List<ItemStack> result, ItemEntry item, int count, int maxStackSize) {
        if (count <= maxStackSize) result.addLast(item.asStack(count));
        else {
            int fullStacks = count / maxStackSize;
            int rest = count - fullStacks * maxStackSize;
            for (int i = 0; i < fullStacks; i++) {
                result.addLast(item.asStack(maxStackSize));
            }
            if (rest > 0) result.addLast(item.asStack(rest));
        }
    }

    // 自动根据物品对象补全最大堆叠数量的重载
    public static void addItemStacksToResult(List<ItemStack> result, ItemEntry item, int count) {
        addItemStacksToResult(result, item, count, item.asItem().getDefaultMaxStackSize());
    }
}
