package cn.lyricraft.createideas.content.tools.portableStressometer;

import cn.lyricraft.lyricore.Lyricore;
import cn.lyricraft.lyricore.network.requestManager.AbstractRequestManager;
import cn.lyricraft.lyricore.network.requestManager.IManagedResponseHandler;
import cn.lyricraft.lyricore.network.requestManager.ManagedRequestBody;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class NetworkStressRequester implements IManagedResponseHandler {
    public static final NetworkStressRequester INS = new NetworkStressRequester();

    public static final long REQUEST_MIN_INTERVAL = 500 * 1_000_000L; // 单位：纳秒（乘号前面是毫秒）
    public static final long COOLDOWN = 600 * 1_000_000L; // 单位：纳秒（务必大于等于 REQUEST_MIN_INTERVAL）

    private static KineticBlockEntity lastBe;
    private static boolean waiting = false;
    private static boolean haveResult = false;
    private static long startTime = 0;
    private static long requestTime = 0;
    private static int lastId = -1;
    private static float lastCapacity = -1;
    private static float lastStress = -1;

    public NetworkStressRequester(){
        Lyricore.SERVER_RESPONSE_MANAGER.registerRequestPair(NetworkStressRequestPair.INS);
    }

    public static void start(){
        if (lastBe == null) return;
        waiting = true;
        haveResult = false;
        startTime = System.nanoTime();
    }

    public static boolean checkBe(KineticBlockEntity be){
        if (be == lastBe) return true;
        lastBe = be;
        waiting = false;
        haveResult = false;
        return false;
    }

    public static boolean isWaiting(){
        return waiting;
    }

    public static boolean haveResult(){
        return haveResult;
    }

    public static boolean hasResultArrived(){
        if (!waiting) return haveResult;
        if (System.nanoTime() - requestTime > REQUEST_MIN_INTERVAL){
            lastId = Lyricore.CLIENT_REQUEST_MANAGER.request(NetworkStressRequestPair.INS,
                    new NetworkStressRequestPair.NetworkStressRequestBody(lastBe.getBlockPos()), INS, true);
            requestTime = System.nanoTime();
        }
        return false;
    }

    @Override
    public void handleResponse(ManagedRequestBody rpBody,
                               IPayloadContext context,
                               AbstractRequestManager.ResponseStatus status,
                               AbstractRequestManager.RequestInfo info) {
        if (status.id() != lastId) return;
        if (status.success() && rpBody instanceof NetworkStressRequestPair.NetworkStressResponseBody body){
            lastStress = body.getStress();
            lastCapacity = body.getCapacity();
        } else {
            lastCapacity = -1;
            lastStress = -1;
        }
        waiting = false;
        haveResult = true;
    }

    public static float getCapacity(){
        if (waiting) return -1;
        return lastCapacity;
    }

    public static float getStress(){
        if (waiting) return -1;
        return lastStress;
    }

    public static long getStartTime(){
        return startTime;
    }
}
