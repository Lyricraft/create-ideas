package cn.lyricraft.createideas.datagen.i18n;

import cn.lyricraft.createideas.CIBlocks;
import cn.lyricraft.createideas.CIItems;
import cn.lyricraft.createideas.CreateIdeas;
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
        
    }
}
