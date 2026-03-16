package cn.lyricraft.createideas.configs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // 实用{
    static {
        BUILDER.comment("实用")
                .push("utility");
    }

    // 便携式应力表
    public static final ModConfigSpec.BooleanValue PORTABLE_STRESSOMETER = BUILDER
            .comment("便携式应力表")
            .worldRestart()
            .define("portableStressometer", true);

    // 坚实置物台
    public static final ModConfigSpec.BooleanValue SOLID_DEPOT = BUILDER
            .comment("坚实置物台")
            .worldRestart()
            .define("solidDepot", true);

    // }实用
    static {
        BUILDER.pop();
    }

    // 配方{
    static {
        BUILDER.comment("配方")
                .push("recipes");
    }

    // 高炉熔炼粗矿块
    public static final ModConfigSpec.BooleanValue BLAST_RECIPES_RAW_ORE_BLOCKS = BUILDER
            .comment("高炉熔炼粗矿块：允许高炉直接熔炼粗铁块、粗金块和粗铜块，这也将使鼓风机批量冶炼可以处理这些粗矿块。",
                    "Enables direct blasting of block of raw copper, raw iron and raw gold. " +
                            "This also extends bulk blasting to process these raw ore blocks.")
            .worldRestart()
            .define("blastRecipesRawOreBlocks", true);

    // 缠魂配方{
    static {
        BUILDER.comment("批量缠魂配方")
                .push("hauntingRecipes");
    }

    // 紫水晶配方
    public static final ModConfigSpec.BooleanValue HAUNTING_RECIPE_AMETHYST_SHARD = BUILDER
            .comment("紫水晶碎片缠魂")
            .worldRestart()
            .define("hauntingRecipeAmethystShard", true);

    // 红砖块配方
    public static final ModConfigSpec.BooleanValue HAUNTING_RECIPE_BRICKS = BUILDER
            .comment("红砖块缠魂")
            .worldRestart()
            .define("hauntingRecipeBricks", true);

    // 青金石块配方
    public static final ModConfigSpec.BooleanValue HAUNTING_RECIPE_LAPIS_BLOCK = BUILDER
            .comment("青金石块缠魂")
            .worldRestart()
            .define("hauntingRecipeLapisBlock", true);

    // }缠魂配方
    static {
        BUILDER.pop();
    }

    // 鼓风机批量处理的额外经验{
    static {
        BUILDER.comment("鼓风机批量处理的额外经验：在进行鼓风机批量处理时，生成额外的经验颗粒作为产物。",
                        "Allow to generate extra experience nugget when bulk item processing.")
                .push("bulkProcessingExtraExp");
    }

    // 批量冶炼
    public static final ModConfigSpec.BooleanValue BULK_BLASTING_EXTRA_EXP = BUILDER
            .comment("批量冶炼")
            .worldRestart()
            .define("bulkBlastingExtraExp",true);

    // 批量烟熏
    public static final ModConfigSpec.BooleanValue BULK_SMOKING_EXTRA_EXP = BUILDER
            .comment("批量烟熏")
            .worldRestart()
            .define("bulkSmokingExtraExp",true);

    // }鼓风机批量处理的额外经验
    static {
        BUILDER.pop();
    }

    // }配方
    static {
        BUILDER.pop();
    }

    // 微调{
    static {
        BUILDER.comment("微调")
                .push("tweaks");
    }

    // 护目镜可在手上使用
    public static final ModConfigSpec.BooleanValue USE_GOGGLES_IN_HAND = BUILDER
            .comment("护目镜可在手上使用：将工程师护目镜拿在主手或副手上时，也可以生效。",
                    "Engineer's goggles would also take effect when held in main hand or off hand.")
            .define("useGogglesInHand", true);

    // 置物台机制{
    static {
        BUILDER.comment("置物台机制")
                .push("depotFeatures");
    }

    // （置物台类型枚举）
    public enum DepotType{
        DISABLED, // 关闭
        SOLID_DEPOT_ONLY, // 仅坚实置物台
        ALL // 始终启用
    }

    // 自动堆叠
    public static final ModConfigSpec.EnumValue<DepotType> DEPOT_AUTO_MERGING = BUILDER
            .comment("自动合并：当置物台上有相同类型的物品且未达到堆叠上限时，自动将后来物品堆叠到置物台上。",
                    "可选值：DISABLED——关闭，SOLID_DEPOT_ONLY——仅坚实置物台，ALL——始终启用。",
                    "When there are already items of the same type on the depot and the stack is not full, new items would be automatically stacked on the depot.")
            .worldRestart()
            .defineEnum("depotAutoMerging", DepotType.SOLID_DEPOT_ONLY);

    // 物品推动
    public static final ModConfigSpec.EnumValue<DepotType> DEPOT_ITEM_PUSHING = BUILDER
            .comment("物品推动：传送带向置物台输送物品时，如置物台上已有物品且无法堆叠，前方有同向传送带等合法去处时，后来物品可将目前物品推向去处，并使自身到达置物台上。",
                    "可选值：DISABLED——关闭，SOLID_DEPOT_ONLY——仅坚实置物台，ALL——始终启用。",
                    "When a conveyor belt is trying to put items on the depot, if there are already items on the depot and they cannot be stacked, and there are valid destinations in the front such as conveyor belts facing the same direction, new items would push current items to the destination and take their place on the depot.")
            .worldRestart()
            .defineEnum("depotItemPushing", DepotType.SOLID_DEPOT_ONLY);

    // 微调{
    static {
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
