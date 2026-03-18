package cn.lyricraft.createideas.datagen;

import cn.lyricraft.createideas.CIConfigs;
import cn.lyricraft.createideas.CreateIdeas;
import cn.lyricraft.createideas.configs.CommonConfig;
import cn.lyricraft.lyricore.conditions.LCConditions;
import cn.lyricraft.lyricore.conditions.configCondition.BooleanConfigCondition;
import cn.lyricraft.lyricore.config.helper.ConfigHelper;
import cn.lyricraft.lyricore.datagen.LCRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CIRecipeProvider extends LCRecipeProvider {

    public CIRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        setNamespace(CreateIdeas.MOD_NAMESPACE);
        // 高炉熔炼
        // 粗铜块
        oreCookingBuilder(OreCookingType.BLASTING, Blocks.RAW_COPPER_BLOCK, Blocks.COPPER_BLOCK, 900, RecipeCategory.MISC, recipeOutput)
                .experience(8.4f)
                .conditions(BooleanConfigCondition.of(CommonConfig.SPEC, CommonConfig.BLAST_RECIPES_RAW_ORE_BLOCKS))
                .path("blasting/from_raw_copper_block")
                .build();
        // 粗金块
        oreCookingBuilder(OreCookingType.BLASTING, Blocks.RAW_GOLD_BLOCK, Blocks.GOLD_BLOCK, 900, RecipeCategory.MISC, recipeOutput)
                .experience(12.0f)
                .conditions(BooleanConfigCondition.of(CommonConfig.SPEC, CommonConfig.BLAST_RECIPES_RAW_ORE_BLOCKS))
                .path("blasting/from_raw_gold_block")
                .build();
        // 粗铁块
        oreCookingBuilder(OreCookingType.BLASTING, Blocks.RAW_IRON_BLOCK, Blocks.IRON_BLOCK, 900, RecipeCategory.MISC, recipeOutput)
                .experience(8.4f)
                .conditions(BooleanConfigCondition.of(CommonConfig.SPEC, CommonConfig.BLAST_RECIPES_RAW_ORE_BLOCKS))
                .path("blasting/from_raw_iron_block")
                .build();
    }
}
