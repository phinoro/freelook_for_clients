package com.pixelstorm.freelook_for_clients.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.pixelstorm.freelook_for_clients.CanFreelook;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Camera.class)
public abstract class CameraMixin {
	@Shadow
	private Entity entity;

	@Shadow
	protected abstract void setRotation(float yRot, float xRot);

	@Redirect(
		method = "alignWithEntity",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"),
		slice= @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPassenger()Z"),
			to = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z")))
	private void setFreelookRotation(Camera self, float yRot, float xRot) {
		if (entity instanceof CanFreelook freelooker
				&& freelooker.getFreelookState().isFreelookingOrInterpolating()) {
			this.setRotation(freelooker.getFreelookYaw(), freelooker.getFreelookPitch());
		} else {
			this.setRotation(yRot, xRot);
		}
	}
}
