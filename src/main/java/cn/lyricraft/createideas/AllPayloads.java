package cn.lyricraft.createideas;

import cn.lyricraft.createideas.configs.SyncConfigPayload;
import cn.lyricraft.createideas.foundation.kinetics.networkStressCommunication.NetworkStressAnswerPayload;
import cn.lyricraft.createideas.foundation.kinetics.networkStressCommunication.NetworkStressAskPayload;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class AllPayloads {
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(CreateIdeas.MOD_VERSION);
        registrar.playToClient(SyncConfigPayload.TYPE, SyncConfigPayload.STREAM_CODEC, SyncConfigPayload::handleClient);
        registrar.playToServer(NetworkStressAskPayload.TYPE, NetworkStressAskPayload.STREAM_CODEC, NetworkStressAskPayload::handleServer);
        registrar.playToClient(NetworkStressAnswerPayload.TYPE, NetworkStressAnswerPayload.STREAM_CODEC, NetworkStressAnswerPayload::handleClient);
    }
}
