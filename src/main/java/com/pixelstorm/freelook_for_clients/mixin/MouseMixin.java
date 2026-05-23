package com.pixelstorm.freelook_for_clients.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.pixelstorm.freelook_for_clients.CanFreelook;
import com.pixelstorm.freelook_for_clients.FreelookForClients;
import com.pixelstorm.freelook_for_clients.FreelookState;

import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(Mouse.class)
public abstract class MouseMixin {
	@Redirect(method = "updateMouse", at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerEntity.changeLookDirection(DD)V"))
	private void changeFreelookDirection(ClientPlayerEntity self, double cursorDeltaX, double cursorDeltaY) {
		// Handle mouse movement, keybinds and starting/stopping freelooking
		CanFreelook freelooker = (CanFreelook) self;

		if (FreelookForClients.holdFreeLookKeybind.isPressed()
				|| FreelookForClients.toggleFreeLookKeybind.isPressed()) {
			freelooker.setFreelookState(FreelookState.Freelooking);
		}else{
			freelooker.setFreelookState(FreelookState.NotFreelooking);
		}

		switch (freelooker.getFreelookState()) {
			case Freelooking:
				freelooker.changeFreelookDirection(cursorDeltaX, cursorDeltaY);
				break;
			case NotFreelooking:
				self.changeLookDirection(cursorDeltaX, cursorDeltaY);
				// When not freelooking, sync pitch & yaw so camera doesn't snap to some other
				// orientation when activating freelooking
				freelooker.setFreelookPitch(self.getPitch());
				freelooker.setFreelookYaw(self.getYaw());
				break;
		}
	}
}
