package cn.lyricraft.createideas.datagen.i18n;

import cn.lyricraft.createideas.CIBlocks;
import cn.lyricraft.createideas.CIItems;
import cn.lyricraft.createideas.CreateIdeas;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class enUsLanProvider extends LanguageProvider {
    public enUsLanProvider(PackOutput output) {
        super(output, CreateIdeas.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // 方块 名称由 机械动力 自动生成
        // 物品 名称由 机械动力 自动生成

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
    }
}
