package cn.lyricraft.createideas.datagen.i18n;

import cn.lyricraft.createideas.CIBlocks;
import cn.lyricraft.createideas.CIItems;
import cn.lyricraft.createideas.CreateIdeas;
import cn.lyricraft.createideas.configs.CommonConfig;
import cn.lyricraft.lyricore.config.helper.ConfigHelper;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class zhCnLanProvider extends LanguageProvider {
    public zhCnLanProvider(PackOutput output) {
        super(output, CreateIdeas.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        // 方块
        add(CIBlocks.SOLID_DEPOT.get(), "坚实置物台");
        // 物品
        add(CIItems.PORTABLE_STRESSOMETER_ITEM.get(), "便携式应力表");

        // GUI 覆盖层
        add("create_ideas.gui.overlay.portable_gauge.info_header", "便携式仪表信息：");
        add("create_ideas.gui.overlay.portable_stressometer.right_click_to_measure", "右击以进行测量");
        add("create_ideas.gui.overlay.portable_stressometer.measuring", "测量中 (%s%%)");
        add("create_ideas.gui.overlay.portable_stressometer.measurement_failed", "测量失败！");
        add("create_ideas.gui.overlay.portable_stressometer.right_click_to_remeasure", "右击以重新测量");

        // 多人游戏
        add("create_ideas.multiplayer.disconnect.unable_to_sync_config", "无法与服务器同步模组《机械动力：好主意》的配置");

        // JEI 提示
        add("create_ideas.jei.tooltip.recipe.extendedBy", "配方扩展：%s");

        // 配置
        add(ConfigHelper.I18n.title(CreateIdeas.MOD_ID), "机械动力：好主意！");
        // Common 配置
        ConfigHelper.I18n commonConfig = ConfigHelper.i18n(CommonConfig.SPEC);
        add(commonConfig.file(), "机械动力：好主意！");
        add(commonConfig.fileTitle(), "机械动力：好主意！");
        add(commonConfig.section("utility"), "实用");
        add(commonConfig.tooltip(), "一些新添加的实用物件及相关调整。");
        add(commonConfig.cv(CommonConfig.PORTABLE_STRESSOMETER), "便携式应力表");
        add(commonConfig.tooltip(), "允许合成和使用便携式应力表。");
        add(commonConfig.section("recipes"), "配方");
        add(commonConfig.tooltip(), "管理添加的新配方以及对已有配方进行的调整。");
        add(commonConfig.cv(CommonConfig.BLAST_RECIPES_RAW_ORE_BLOCKS), "高炉熔炼粗矿块");
        add(commonConfig.tooltip(), "允许高炉直接熔炼粗铁块、粗金块和粗铜块，这也将使鼓风机批量冶炼可以处理这些粗矿块。");
        add(commonConfig.section("hauntingRecipes"), "批量缠魂配方");
        add(commonConfig.tooltip(), "新添加的鼓风机批量缠魂配方。");
        add(commonConfig.cv(CommonConfig.HAUNTING_RECIPE_AMETHYST_SHARD), "紫水晶碎片缠魂");
        add(commonConfig.tooltip(), "启用紫水晶碎片批量缠魂转化为回响碎片的配方。");
        add(commonConfig.cv(CommonConfig.HAUNTING_RECIPE_BRICKS), "红砖块缠魂");
        add(commonConfig.tooltip(), "启用红砖块批量缠魂转化为红色下界砖块的配方。");
        add(commonConfig.cv(CommonConfig.HAUNTING_RECIPE_LAPIS_BLOCK), "青金石块缠魂");
        add(commonConfig.tooltip(), "启用青金石块批量缠魂转化为海晶石和海晶砂粒的配方。");
        add(commonConfig.section("bulkProcessingExtraExp"), "鼓风机批量处理的额外经验");
        add(commonConfig.tooltip(), "在进行鼓风机批量处理时，生成额外的经验颗粒作为产物。");
        add(commonConfig.cv(CommonConfig.BULK_BLASTING_EXTRA_EXP), "批量熔炼");
        add(commonConfig.tooltip(), "使鼓风机批量熔炼能生成经验颗粒。");
        add(commonConfig.cv(CommonConfig.BULK_SMOKING_EXTRA_EXP), "批量烟熏");
        add(commonConfig.tooltip(), "使鼓风机批量烟熏能生成经验颗粒。");
    }
}
