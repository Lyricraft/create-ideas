package cn.lyricraft.createideas.configs.syncConfig;

import cn.lyricraft.createideas.CreateIdeas;
import cn.lyricraft.lyricore.network.requestManager.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncConfigRequestPair extends ServerRequestPair<SyncConfigRequestPair.SyncConfigRequestBody, SyncConfigRequestPair.SyncConfigResponseBody> {
    public static final SyncConfigRequestPair INS = new SyncConfigRequestPair();

    @Override
    public ResourceLocation type() {
        return ResourceLocation.fromNamespaceAndPath(CreateIdeas.MOD_NAMESPACE, "sync_config_request");
    }

    @Override
    protected void handleRequest(SyncConfigRequestBody requestBody, IPayloadContext context, ClientResponseManager.Handle handle) {
        if (context.player().isLocalPlayer()) return;
        String syncId = requestBody.getSyncId();
        if (syncId.isEmpty()) {
            CreateIdeas.LOGGER.error("收到无效的配置同步请求。Received invalid config sync request.");
            handle.reject();
        }
        SyncConfig.fromNbt(requestBody.getConfig());
        handle.response(new SyncConfigResponseBody(syncId));
        CreateIdeas.LOGGER.info("已从服务端同步必要配置项。Synced necessary config items from Server.");
    }

    @Override
    public SyncConfigRequestBody requestBodyFromNbt(CompoundTag bodyNbt) {
        return new SyncConfigRequestBody(bodyNbt);
    }

    @Override
    public SyncConfigResponseBody responseBodyFromNbt(CompoundTag bodyNbt) {
        return new SyncConfigResponseBody(bodyNbt);
    }

    @Override
    public SyncConfigResponseBody emptyResponseBody() {
        return new SyncConfigResponseBody();
    }

    public static class SyncConfigRequestBody extends ManagedRequestBody{
        public CompoundTag config;
        private String syncId;

        public SyncConfigRequestBody(CompoundTag config, String syncId){
            this.config = config;
            this.syncId = syncId;
        }

        public SyncConfigRequestBody(CompoundTag nbt){
            syncId = nbt.getString("syncId");
            config = nbt.copy();
            config.remove("syncId");
        }

        public SyncConfigRequestBody(){
            config = new CompoundTag();
            syncId = "";
        }

        @Override
        public CompoundTag toNbt() {
            CompoundTag bodyNbt = config.copy();
            bodyNbt.putString("syncId", syncId);
            return bodyNbt;
        }

        public CompoundTag getConfig(){
            return config;
        }

        public String getSyncId(){
            return syncId;
        }
    }

    public static class SyncConfigResponseBody extends ManagedRequestBody{
        private final String syncId;

        public SyncConfigResponseBody(String syncId){
            this.syncId = syncId;
        }

        public SyncConfigResponseBody(CompoundTag nbt){
            syncId = nbt.getString("syncId");
        }

        public SyncConfigResponseBody(){
            syncId = "";
        }

        @Override
        public CompoundTag toNbt() {
            CompoundTag bodyNbt = new CompoundTag();
            bodyNbt.putString("syncId", syncId);
            return bodyNbt;
        }

        public String getSyncId(){
            return syncId;
        }
    }
}
