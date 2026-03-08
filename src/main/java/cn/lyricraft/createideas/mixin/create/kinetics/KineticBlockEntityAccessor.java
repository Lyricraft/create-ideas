package cn.lyricraft.createideas.mixin.create.kinetics;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KineticBlockEntity.class)
public interface KineticBlockEntityAccessor {
    @Accessor("capacity")
    public float getCapacity();

    @Accessor("stress")
    public float getStress();

    @Accessor("overStressed")
    public boolean overStressed();
}
