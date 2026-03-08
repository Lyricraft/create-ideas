package cn.lyricraft.createideas;

public final class Constants {

    // 经验颗粒相关
    // 经验颗粒价值，即使用一个经验颗粒获得多少点经验，影响一些配方经验颗粒产出的概率与数量
    public static final int CREATE_EXP_NUGGET_VALUE = 3;

    // 鼓风机批量处理经验颗粒生成
    // 经验倍率，指生成经验颗粒等价于原版配方经验值的多少倍
    public static final float BULK_BLASTING_EXTRA_EXP_RATIO = 2.25f; // 高炉配方的经验倍率
    public static final float BULK_SMELTING_EXTRA_EXP_RATIO = 1.25f; // 熔炉配方的经验倍率（高炉与烟熏炉以外的剩余熔炼配方）
    public static final float BULK_SMOKING_EXTRA_EXP_RATIO = 1.75f; // 烟熏炉配方的经验倍率
    // 鼓风机批量处理的最大经验产出概率，如大于此值则调整单次产出数量而非使用更高概率。
    public static final float BULK_PROCESSING_EXTRA_EXP_MAX_P = 0.75f;

}
