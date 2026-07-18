package com.pixelstorm.freelook_for_clients.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pixelstorm.freelook_for_clients.CanFreelook;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import com.mojang.math.Constants;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Accessor
	public abstract Camera getMainCamera();


	@Inject(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;submitHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/player/LocalPlayer;I)V"))
	private void modifyHandMatrix(CallbackInfo ci, @Local(argsOnly = true, name = "deltaPartialTick") float deltaPartialTick, @Local(name = "poseStack") LocalRef<PoseStack> poseStackLocalRef) {
		PoseStack matrices = poseStackLocalRef.get();

		Entity entity = this.getMainCamera().entity();
		if (entity instanceof CanFreelook freelooker && freelooker.getFreelookState().isFreelookingOrInterpolating()) {
			// Rotate the player hand/held item so it appears to remain fixed in space while
			// freelooking, to emphasize that the player is freelooking, and thus the aim
			// vector for clicking on stuff is fixed
			float yawDiff = (freelooker.getFreelookYaw() - entity.getViewYRot(deltaPartialTick))
				* Constants.DEG_TO_RAD;

			float pitchDiff = (freelooker.getFreelookPitch() - entity.getViewXRot(deltaPartialTick))
				* Constants.DEG_TO_RAD;

			Vector3f camera_local_up = new Vector3f(0f, 1f, 0f)
				.rotateX(freelooker.getFreelookPitch() * Constants.DEG_TO_RAD);

			matrices.mulPose(new Quaternionf().rotationAxis(yawDiff, camera_local_up));
			matrices.mulPose(new Quaternionf().rotationX(pitchDiff));
			poseStackLocalRef.set(matrices);
		}
	}
}
