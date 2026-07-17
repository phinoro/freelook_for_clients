package com.pixelstorm.freelook_for_clients.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;
import com.pixelstorm.freelook_for_clients.CanFreelook;
import com.pixelstorm.freelook_for_clients.FreelookState;

// Holds state for freelooking to communicate between other mixins
@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayer implements CanFreelook {
	private float freelookPitch;
	private float freelookYaw;
	private FreelookState freelookState;

	public ClientPlayerEntityMixin(ClientLevel world, GameProfile profile) {
		super(world, profile);
		throw new AssertionError();
	}

	@Inject(method = "<init>*", at = @At("RETURN"))
	private void onConstruct(CallbackInfo ci) {
		freelookPitch = 0f;
		freelookYaw = 0f;
		freelookState = FreelookState.NotFreelooking;
	}

	@Override
	public void changeFreelookDirection(double cursorDeltaX, double cursorDeltaY) {
		// Copied from Entity::changeLookDirection
		float pitchDelta = (float) cursorDeltaY * 0.15f;
		this.setFreelookPitch(Mth.clamp(this.getFreelookPitch() + pitchDelta, -90f, 90f));

		float yawDelta = (float) cursorDeltaX * 0.15f;
		this.setFreelookYaw(this.getFreelookYaw() + yawDelta);
	}

	@Override
	public void setFreelookPitch(float pitch) {
		this.freelookPitch = pitch;
	}

	@Override
	public float getFreelookPitch() {
		return this.freelookPitch;
	}

	@Override
	public void setFreelookYaw(float yaw) {
		this.freelookYaw = yaw;
	}

	@Override
	public float getFreelookYaw() {
		return this.freelookYaw;
	}

	@Override
	public FreelookState getFreelookState() {
		return this.freelookState;
	}

	@Override
	public void setFreelookState(FreelookState state) {
		if (!this.freelookState.isFreelooking() && state.isFreelooking()) {
			this.sendOverlayMessage(Component.translatable("message.freelook_for_clients.enabled"));
		} else if (this.freelookState.isFreelooking() && !state.isFreelooking()) {
			this.sendOverlayMessage(Component.translatable("message.freelook_for_clients.disabled"));
		}
		this.freelookState = state;
	}
}
