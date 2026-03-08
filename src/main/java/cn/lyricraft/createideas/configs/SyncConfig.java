package cn.lyricraft.createideas.configs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class SyncConfig {
    private static List<SyncValue> values = new ArrayList<>();
    private static Builder BUILDER = new Builder();

    // 便携式应力表
    public static final BooleanValue PORTABLE_STRESSOMETER = BUILDER
            .define("common.utility.portableStressometer", false, CommonConfig.PORTABLE_STRESSOMETER);

    // 护目镜可在手上使用
    public static final BooleanValue USE_GOGGLES_IN_HAND = BUILDER
            .define("common.tweaks.useGogglesInHand", false, CommonConfig.USE_GOGGLES_IN_HAND);

    public static void fromNbt(CompoundTag nbt){
        values.forEach(value -> {
            value.nbtGet(nbt);
        });
    }

    public static void toNbt(CompoundTag nbt){
        values.forEach(value -> {
            value.nbtPut(nbt);
        });
    }

    public static void fromLocal(){
        values.forEach(SyncValue::localGet);
    }

    public static void fromLocalWithoutRestart(){
        values.forEach(value -> {
            if (value.needRestart) return;
            else value.localGet();
        });
    }

    private static final class Builder {
        static class Context {
            protected static boolean mustMatch;
            protected static boolean needRestart;

            static {
                clear();
            }

            public static void clear(){
                mustMatch = false;
                needRestart = false;
            }

        }

        public BooleanValue define(String key, boolean defaultValue, ModConfigSpec.BooleanValue localValue){
            BooleanValue value = new BooleanValue(key, defaultValue, localValue, Context.mustMatch, Context.needRestart);
            values.addLast(value);
            Context.clear();
            return value;
        }

        public Builder mustMatch(){
            Context.mustMatch = true;
            return this;
        }

        public Builder needRestart(){
            Context.needRestart = true;
            return this;
        }

    }

    public static abstract class SyncValue<T, V>{
        protected String key;
        protected T value;
        private T defaultValue;
        protected V localValue;
        private boolean mustMatch;
        private boolean needRestart;
        private SyncValue(String key, T defaultValue, V localValue, boolean mustMatch, boolean needRestart){
            value = this.defaultValue = defaultValue;
            this.localValue = localValue;
            this.mustMatch = mustMatch;
            this.needRestart = needRestart;
        }
        public T get(){
            return value;
        }
        protected void set(T newValue){
            value = newValue;
        }
        protected void setDefault(){
            value = defaultValue;
        }
        protected abstract void nbtPut(CompoundTag nbt);
        protected abstract void nbtGet(CompoundTag nbt);
        protected abstract void localGet();
        public abstract boolean matchLocal();
        public boolean getMustMatch(){
            return mustMatch;
        }
        public boolean getNeedRestart(){
            return needRestart;
        }
    }

    public static class BooleanValue extends SyncValue<Boolean, ModConfigSpec.BooleanValue>{
        private BooleanValue(String key, boolean defaultValue, ModConfigSpec.BooleanValue localValue, boolean mustMatch, boolean needRestart) {
            super(key, defaultValue, localValue, mustMatch, needRestart);
        }

        protected void nbtPut(CompoundTag nbt){
            nbt.putBoolean(key, value);
        }

        protected void nbtGet(CompoundTag nbt){
            if(nbt.contains(key, Tag.TAG_BYTE)) set(nbt.getBoolean(key));
            else setDefault();
        }

        protected void localGet(){
            value = localValue.isTrue();
        }

        public boolean matchLocal(){
            return (value == localValue.isTrue());
        }
    }
}