package cn.lyricraft.createideas.foundation.kinetics.networkStressCommunication;

import cn.lyricraft.createideas.CreateIdeas;
import cn.lyricraft.createideas.configs.SyncConfig;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.TorquePropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record NetworkStressAskPayload(CompoundTag blockPosNbt) implements CustomPacketPayload {
    public static final Type<NetworkStressAskPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CreateIdeas.MOD_NAMESPACE,
                    "network_stress_ask_payload"));

    public static final StreamCodec<ByteBuf, NetworkStressAskPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            NetworkStressAskPayload::blockPosNbt,
            NetworkStressAskPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleServer(IPayloadContext context) {
        BlockPos pos = getBlockPos(blockPosNbt);
        if (pos == null) return;
        BlockEntity be = context.player().level().getBlockEntity(pos);
        if (be instanceof KineticBlockEntity kineticBe){
            KineticNetwork network = Create.TORQUE_PROPAGATOR.getOrCreateNetworkFor(kineticBe);
            if(!(network == null)){
                PacketDistributor.sendToPlayer((ServerPlayer) context.player(), new NetworkStressAnswerPayload(NetworkStressAnswerPayload.putAnswer(
                        network.calculateCapacity(),
                        network.calculateStress()
                )));
            } else return;
        } else return;
    }

    public static CompoundTag putBlockPos(BlockPos pos){
        CompoundTag nbt = new CompoundTag();
        nbt.putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
        return nbt;
    }

    public static BlockPos getBlockPos(CompoundTag nbt){
        int[] pos = nbt.getIntArray("pos");
        if (pos.length == 3) return new BlockPos(pos[0], pos[1], pos[2]);
        else return null;
    }
}
