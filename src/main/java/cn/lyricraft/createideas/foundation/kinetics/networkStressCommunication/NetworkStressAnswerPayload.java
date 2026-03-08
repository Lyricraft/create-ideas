package cn.lyricraft.createideas.foundation.kinetics.networkStressCommunication;

import cn.lyricraft.createideas.CreateIdeas;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record NetworkStressAnswerPayload(CompoundTag resultNbt) implements CustomPacketPayload {
    public static final Type<NetworkStressAnswerPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CreateIdeas.MOD_NAMESPACE,
                    "network_stress_answer_payload"));

    public static final StreamCodec<ByteBuf, NetworkStressAnswerPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            NetworkStressAnswerPayload::resultNbt,
            NetworkStressAnswerPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handleClient(IPayloadContext context) {
        NetworkStressAsker.lastAnswer = getAnswer(resultNbt);
        NetworkStressAsker.resultArrived = true;
    }

    public static CompoundTag putAnswer(float capacity, float stress){
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("capacity", capacity);
        nbt.putFloat("stress", stress);
        return nbt;
    }

    public static Answer getAnswer(CompoundTag nbt){
        return new Answer(nbt.getFloat("capacity"), nbt.getFloat(("stress")));
    }

    public record Answer(float capacity, float stress) {
    }
}
