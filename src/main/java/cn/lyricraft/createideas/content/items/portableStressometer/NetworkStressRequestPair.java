package cn.lyricraft.createideas.content.items.portableStressometer;

import cn.lyricraft.createideas.CreateIdeas;
import cn.lyricraft.lyricore.network.requestManager.ClientRequestPair;
import cn.lyricraft.lyricore.network.requestManager.ManagedRequestBody;
import cn.lyricraft.lyricore.network.requestManager.ServerResponseManager;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public class NetworkStressRequestPair extends ClientRequestPair<NetworkStressRequestPair.NetworkStressRequestBody, NetworkStressRequestPair.NetworkStressResponseBody> {
    public static final NetworkStressRequestPair INS = new NetworkStressRequestPair();

    @Override
    public ResourceLocation type() {
        return ResourceLocation.fromNamespaceAndPath(CreateIdeas.MOD_NAMESPACE, "network_stress_request");
    }

    @Override
    protected void handleRequest(NetworkStressRequestBody requestBody, IPayloadContext context, ServerResponseManager.Handle handle) {
        BlockPos pos = requestBody.getPos();
        if (pos == null) {
            handle.reject();
            return;
        };
        BlockEntity be = context.player().level().getBlockEntity(pos);
        if (be instanceof KineticBlockEntity kineticBe){
            KineticNetwork network = Create.TORQUE_PROPAGATOR.getOrCreateNetworkFor(kineticBe);
            if(!(network == null)){
                handle.response(new NetworkStressResponseBody(network.calculateCapacity(), network.calculateStress()));
                return;
            } else {
                handle.response(new NetworkStressResponseBody(0, 0));
                return;
            }
        }
        handle.reject();
    }

    @Override
    public NetworkStressRequestBody requestBodyFromNbt(CompoundTag bodyNbt) {
        return new NetworkStressRequestBody(bodyNbt);
    }

    @Override
    public NetworkStressResponseBody responseBodyFromNbt(CompoundTag bodyNbt) {
        return new NetworkStressResponseBody(bodyNbt);
    }

    @Override
    public NetworkStressResponseBody emptyResponseBody() {
        return new NetworkStressResponseBody();
    }


    public static class NetworkStressRequestBody extends ManagedRequestBody{
        private BlockPos pos;

        public NetworkStressRequestBody(BlockPos pos){
            this.pos = pos;
        }

        public NetworkStressRequestBody(CompoundTag nbt){
            int[] posArray = nbt.getIntArray("pos");
            if (posArray.length == 3) pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
            else pos = null;
        }

        public NetworkStressRequestBody(){
            pos = null;
        }

        @Override
        public CompoundTag toNbt() {
            if (pos != null){
                CompoundTag bodyNbt = new CompoundTag();
                bodyNbt.putIntArray("pos", List.of(pos.getX(), pos.getY(), pos.getZ()));
                return bodyNbt;
            } else return new CompoundTag();
        }

        public BlockPos getPos(){
            return pos;
        }
    }

    public static class NetworkStressResponseBody extends ManagedRequestBody{
        private float capacity;
        private float stress;

        public NetworkStressResponseBody(float capacity, float stress){
            this.capacity = capacity;
            this.stress = stress;
        }

        public NetworkStressResponseBody(CompoundTag nbt){
            capacity = nbt.getFloat("capacity");
            stress = nbt.getFloat("stress");
        }

        public NetworkStressResponseBody(){
            capacity = -1;
            stress = 0;
        }

        @Override
        public CompoundTag toNbt() {
            CompoundTag bodyNbt = new CompoundTag();
            bodyNbt.putFloat("capacity", capacity);
            bodyNbt.putFloat("stress", stress);
            return bodyNbt;
        }

        public float getCapacity(){
            return capacity;
        }

        public float getStress(){
            return stress;
        }
    }
}
