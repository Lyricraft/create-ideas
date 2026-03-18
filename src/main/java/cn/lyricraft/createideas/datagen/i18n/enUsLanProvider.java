package cn.lyricraft.createideas.datagen.i18n;

import cn.lyricraft.createideas.CIBlocks;
import cn.lyricraft.createideas.CIConfigs;
import cn.lyricraft.createideas.CIItems;
import cn.lyricraft.createideas.CreateIdeas;
import cn.lyricraft.createideas.configs.CommonConfig;
import cn.lyricraft.lyricore.config.helper.ConfigHelper;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class enUsLanProvider extends LanguageProvider {
    public enUsLanProvider(PackOutput output) {
        super(output, CreateIdeas.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // 方块 名称由 机械动力 自动生成
        add(CIBlocks.SOLID_DEPOT.get(), "Solid Depot");
        // 物品 名称由 机械动力 自动生成
        add(CIItems.PORTABLE_STRESSOMETER_ITEM.get(), "Portable Stressometer");

        // GUI 覆盖层
        add("create_ideas.gui.overlay.portable_gauge.info_header", "Portable Gauge Information:");
        add("create_ideas.gui.overlay.portable_stressometer.right_click_to_measure", "Right-click to Measure");
        add("create_ideas.gui.overlay.portable_stressometer.measuring", "Measuring (%s%%)");
        add("create_ideas.gui.overlay.portable_stressometer.measurement_failed", "Measurement Failed!");
        add("create_ideas.gui.overlay.portable_stressometer.right_click_to_remeasure", "Right-click to Try Again");

        // 多人游戏
        add("create_ideas.multiplayer.disconnect.unable_to_sync_config", "Unable to sync Create: Ideas! config with server");

        // JEI 提示
        add("create_ideas.jei.tooltip.recipe.extendedBy", "Recipe Extended By: %s");

        // 配置
        add(ConfigHelper.I18n.title(CreateIdeas.MOD_ID), "Create: Ideas!");
        // Common 配置
        ConfigHelper.I18n commonConfig = ConfigHelper.i18n(CommonConfig.SPEC);
        add(commonConfig.file(), "Create: Ideas!");
        add(commonConfig.fileTitle(), "Create: Ideas!");
        add(commonConfig.section("utility"), "Utility");
        add(commonConfig.sectionTooltip("utility"), "Some practical new tools and adjustments for them.");
        add(commonConfig.cv(CommonConfig.PORTABLE_STRESSOMETER), "Portable Stressometer");
        add(commonConfig.cvTooltip(CommonConfig.PORTABLE_STRESSOMETER), "Allow to craft portable stressometer, as well as use it.");
        add(commonConfig.section("recipes"), "Recipes");
        add(commonConfig.sectionTooltip("recipes"), "Some new recipes added and some tweaks for current recipes.");
        add(commonConfig.cv(CommonConfig.BLAST_RECIPES_RAW_ORE_BLOCKS), "Blasting Raw Ore Blocks Recipe");
        add(commonConfig.cvTooltip(CommonConfig.BLAST_RECIPES_RAW_ORE_BLOCKS), "Enables direct blasting of block of raw copper, raw iron and raw gold. This also extends bulk blasting to process these raw ore blocks.");
        add(commonConfig.section("hauntingRecipes"), "Haunting Recipes");
        add(commonConfig.sectionTooltip("hauntingRecipes"), "Some new bulk haunting recipes.");
        add(commonConfig.cv(CommonConfig.HAUNTING_RECIPE_AMETHYST_SHARD), "Haunting Amethyst Shard");
        add(commonConfig.cvTooltip(CommonConfig.HAUNTING_RECIPE_AMETHYST_SHARD), "Enable the recipe for bulk haunting amethyst shard into echo shard.");
        add(commonConfig.cv(CommonConfig.HAUNTING_RECIPE_BRICKS), "Haunting Bricks");
        add(commonConfig.cvTooltip(CommonConfig.HAUNTING_RECIPE_BRICKS), "Enable the recipe for bulk haunting bricks into red nether bricks.");
        add(commonConfig.cv(CommonConfig.HAUNTING_RECIPE_LAPIS_BLOCK), "Haunting Block of Lapis");
        add(commonConfig.cvTooltip(CommonConfig.HAUNTING_RECIPE_LAPIS_BLOCK), "Enable the recipe for bulk block of lapis into prismarine and prismarine crystals.");
        add(commonConfig.section("bulkProcessingExtraExp"), "Bulk Processing Extra Experience Nuggets");
        add(commonConfig.sectionTooltip("bulkProcessingExtraExp"), "Allow to generate extra experience nugget when bulk item processing.");
        add(commonConfig.cv(CommonConfig.BULK_BLASTING_EXTRA_EXP), "Bulk Blasting");
        add(commonConfig.cvTooltip(CommonConfig.BULK_BLASTING_EXTRA_EXP), "Enable the feature for Bulk Blasting.");
        add(commonConfig.cv(CommonConfig.BULK_SMOKING_EXTRA_EXP), "Bulk Smoking");
        add(commonConfig.cvTooltip(CommonConfig.BULK_SMOKING_EXTRA_EXP), "Enable the feature for Bulk Smoking.");

    }
}
