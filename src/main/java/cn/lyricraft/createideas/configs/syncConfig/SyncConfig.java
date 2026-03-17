package cn.lyricraft.createideas.configs.syncConfig;

import cn.lyricraft.createideas.CreateIdeas;
import cn.lyricraft.createideas.configs.CommonConfig;
import cn.lyricraft.lyricore.Lyricore;
import cn.lyricraft.lyricore.log.LogHelper;
import cn.lyricraft.lyricore.network.requestManager.AbstractRequestManager;
import cn.lyricraft.lyricore.network.requestManager.IManagedResponseHandler;
import cn.lyricraft.lyricore.network.requestManager.ManagedRequestBody;
import cn.lyricraft.lyricore.server.typeHelper.ServerTypeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SyncConfig {
    private static final List<SyncValue> values = new ArrayList<>();
    private static final Builder BUILDER = new Builder();
    public static Syncer SYNCER = new Syncer();

    // 坚实置物台
    public static final BooleanValue SOLID_DEPOT = BUILDER
            .needRestart()
            .define("common.utility.solidDepot", false, CommonConfig.SOLID_DEPOT);

    // 便携式应力表
    public static final BooleanValue PORTABLE_STRESSOMETER = BUILDER
            .needRestart()
            .define("common.utility.portableStressometer", false, CommonConfig.PORTABLE_STRESSOMETER);

    // 护目镜可在手上使用
    public static final BooleanValue USE_GOGGLES_IN_HAND = BUILDER
            .needRestart()
            .define("common.tweaks.useGogglesInHand", false, CommonConfig.USE_GOGGLES_IN_HAND);

    public static void fromNbt(CompoundTag nbt){
        values.forEach(value -> {
            value.nbtGet(nbt);
        });
    }

    public static CompoundTag toNbt(){
        CompoundTag nbt = new CompoundTag();
        values.forEach(value -> {
            value.nbtPut(nbt);
        });
        return nbt;
    }

    public static void fromLocal(){
        SYNCER.nextId();
        values.forEach(SyncValue::localGet);
    }

    public static void fromLocalWithoutRestart(){
        SYNCER.nextId();
        values.forEach(value -> {
            if (value.needRestart) return;
            else value.localGet();
        });
    }

    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading event){
        fromLocalWithoutRestart();
        Lyricore.SERVER_REQUEST_MANAGER.requestToAll(
                SyncConfigRequestPair.INS,
                new SyncConfigRequestPair.SyncConfigRequestBody(toNbt(), SYNCER.getSyncId()),
                SYNCER,
                true);
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
            this.key = key;
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

    public static class Syncer implements IManagedResponseHandler {
        private String syncId;
        private List<String> syncedPlayers = new ArrayList<>();

        protected void nextId(){
            syncId = UUID.randomUUID().toString();
            syncedPlayers.clear();
        }

        protected String getSyncId() {
            return syncId;
        }

        protected Syncer(){
            Lyricore.CLIENT_RESPONSE_MANAGER.registerRequestPair(SyncConfigRequestPair.INS);
        }

        @Override
        public void handleResponse(ManagedRequestBody rpBody,
                                   IPayloadContext context,
                                   AbstractRequestManager.ResponseStatus status,
                                   AbstractRequestManager.RequestInfo info) {
            if (context == null) return;
            if (ServerTypeHelper.isLocalPlayer(context.player())) return; // 跳过本地玩家
            if (!status.success()){
                // 状态并非成功
                if (status.equals(AbstractRequestManager.ResponseStatus.Status.TIMEOUT)){
                    // 超时
                    if (syncedPlayers.contains(context.player().getUUID().toString())) return; // 已经同步成功了，可能是网络波动导致的误报，跳过
                    else unableToSync (context, "玩家同步配置超时 / Player sync config timeout");
                } else unableToSync(context, "玩家在同步配置时返回了意料之外的状态 / Player returned an unexpected status when syncing config");
            }
            if (rpBody instanceof SyncConfigRequestPair.SyncConfigResponseBody responseBody){
                if (!responseBody.getSyncId().equals(syncId)) return; // 跳过过期的 SyncId
                else if (!syncedPlayers.contains(context.player().getUUID().toString()))
                    syncedPlayers.add(context.player().getUUID().toString()); // 加到已同步列表
            }
            else unableToSync(context, "玩家在同步配置时返回了无效的响应体 / Player returned an invalid body when syncing config");
        }

        private void unableToSync(IPayloadContext context, String reason){
            CreateIdeas.LOGGER.warn(reason + ": " + LogHelper.playerProfile(context.player()));
            context.disconnect(Component.translatable("lyricore.multiplayer.disconnect.unable_to_sync_config"));
        }

        @SubscribeEvent
        public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
            if (ServerTypeHelper.isLocalPlayer(event.getEntity())) return;
            else Lyricore.SERVER_REQUEST_MANAGER.request(
                    SyncConfigRequestPair.INS,
                    (ServerPlayer) event.getEntity(),
                    new SyncConfigRequestPair.SyncConfigRequestBody(toNbt(), SYNCER.getSyncId()),
                    SYNCER,
                    true);
        }
    }
}