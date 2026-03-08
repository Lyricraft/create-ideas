package cn.lyricraft.createideas.foundation.kinetics.networkStressCommunication;

import cn.lyricraft.createideas.Constants;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class NetworkStressAsker {
    static KineticBlockEntity lastBe;
    static long lastTime;
    static NetworkStressAnswerPayload.Answer lastAnswer;
    static boolean resultArrived = false;

    static public int getCooldownTime(){
        return Constants.NETWORK_STRESS_ASKER_COOLDOWN_TICKS;
    }

    static public int getRestTime(){
        if (getCooldownTime() - (Minecraft.getInstance().player.level().getGameTime() - lastTime) < 0) return 0;
        else return (int) (getCooldownTime() - (Minecraft.getInstance().player.level().getGameTime() - lastTime));
    }

    public static boolean ask(KineticBlockEntity be){
        long pastTime = Minecraft.getInstance().player.level().getGameTime() - lastTime;
        if (lastBe == be) {
            if (pastTime > Constants.NETWORK_STRESS_ASKER_CACHE_TICKS
            && pastTime > Constants.NETWORK_STRESS_ASKER_COOLDOWN_TICKS) {
                request(be);
                return false;
            } else return resultArrived;
        } else {
            if (pastTime > Constants.NETWORK_STRESS_ASKER_COOLDOWN_TICKS) request(be);
            return false;
        }
    }

    public static boolean reAsk(KineticBlockEntity be){
        lastBe = null;
        return(ask(be));
    }

    private static void request(KineticBlockEntity be){
        resultArrived = false;
        lastBe = be;
        PacketDistributor.sendToServer(new NetworkStressAskPayload(NetworkStressAskPayload.putBlockPos(be.getBlockPos())));
        lastTime = Minecraft.getInstance().player.level().getGameTime();
    }

    public static NetworkStressAnswerPayload.Answer getLastAnswer(){
        return lastAnswer;
    }
}
