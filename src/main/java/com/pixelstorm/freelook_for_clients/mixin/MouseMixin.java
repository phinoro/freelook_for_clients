package com.pixelstorm.freelook_for_clients.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.pixelstorm.freelook_for_clients.CanFreelook;
import com.pixelstorm.freelook_for_clients.FreelookForClients;
import com.pixelstorm.freelook_for_clients.FreelookState;

import net.minecraft.client.player.LocalPlayer;

@Mixin(MouseHandler.class)
public abstract class MouseMixin {
	@Redirect(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
	private void changeFreelookDirection(LocalPlayer self, double cursorDeltaX, double cursorDeltaY) {
		// Handle mouse movement, keybinds and starting/stopping freelooking
		CanFreelook freelooker = (CanFreelook) self;

		if (FreelookForClients.holdFreeLookKeybind.isDown()
				|| FreelookForClients.toggleFreeLookKeybind.isDown()) {
			freelooker.setFreelookState(FreelookState.Freelooking);
		}else{
			freelooker.setFreelookState(FreelookState.NotFreelooking);
		}

		switch (freelooker.getFreelookState()) {
			case Freelooking:
				freelooker.changeFreelookDirection(cursorDeltaX, cursorDeltaY);
				break;
			case NotFreelooking:
				self.turn(cursorDeltaX, cursorDeltaY);
				// When not freelooking, sync pitch & yaw so camera doesn't snap to some other
				// orientation when activating freelooking
				freelooker.setFreelookPitch(self.getXRot());
				freelooker.setFreelookYaw(self.getYRot());
				break;
		}
	}
}
