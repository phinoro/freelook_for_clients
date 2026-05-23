package com.pixelstorm.freelook_for_clients.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pixelstorm.freelook_for_clients.CanFreelook;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathConstants;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Accessor
	public abstract Camera getCamera();


	@Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"))
	private void modifyHandMatrix(CallbackInfo ci, @Local(argsOnly = true) float tickDelta, @Local LocalRef<MatrixStack> matrixStackLocalRef) {
		MatrixStack matrices = matrixStackLocalRef.get();

		Entity entity = this.getCamera().getFocusedEntity();
		if (entity instanceof CanFreelook freelooker && freelooker.getFreelookState().isFreelookingOrInterpolating()) {
			// Rotate the player hand/held item so it appears to remain fixed in space while
			// freelooking, to emphasise that the player is freelooking, and thus the aim
			// vector for clicking on stuff is fixed
			float yawDiff = (freelooker.getFreelookYaw() - entity.getYaw(tickDelta))
				* MathConstants.RADIANS_PER_DEGREE;

			float pitchDiff = (freelooker.getFreelookPitch() - entity.getPitch(tickDelta))
				* MathConstants.RADIANS_PER_DEGREE;

			Vector3f camera_local_up = new Vector3f(0f, 1f, 0f)
				.rotateX(freelooker.getFreelookPitch() * MathConstants.RADIANS_PER_DEGREE);

			matrices.multiply(new Quaternionf().rotationAxis(yawDiff, camera_local_up));
			matrices.multiply(new Quaternionf().rotationX(pitchDiff));
			matrixStackLocalRef.set(matrices);
		}
	}
}
