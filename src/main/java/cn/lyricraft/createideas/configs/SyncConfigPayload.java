package cn.lyricraft.createideas.configs;

import cn.lyricraft.createideas.CreateIdeas;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncConfigPayload(CompoundTag configNbt) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncConfigPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CreateIdeas.MOD_NAMESPACE,
                    "sync_config_payload"));

    public static final StreamCodec<ByteBuf, SyncConfigPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            SyncConfigPayload::configNbt,
            SyncConfigPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleClient(IPayloadContext context) {
        if (Minecraft.getInstance().isLocalServer()) return;
        SyncConfig.fromNbt(configNbt);
    }
}
