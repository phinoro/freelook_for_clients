package com.pixelstorm.freelook_for_clients;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;

public class FreelookForClients implements ClientModInitializer {

	private static final KeyMapping.Category FREELOOK_CATEGORY = KeyMapping.Category.register(Identifier.parse("freelook_for_clients"));

	public static KeyMapping holdFreeLookKeybind;

	public static KeyMapping toggleFreeLookKeybind;

	@Override
	public void onInitializeClient() {
		holdFreeLookKeybind = KeyMappingHelper.registerKeyMapping(
			new KeyMapping("key.freelook_for_clients.hold", InputConstants.Type.KEYBOARD, InputConstants.KEY_LALT,
				FREELOOK_CATEGORY));
		toggleFreeLookKeybind = KeyMappingHelper.registerKeyMapping(
			new ToggleKeyMapping("key.freelook_for_clients.toggle", InputConstants.Type.KEYBOARD, InputConstants.UNKNOWN.getValue(),
				FREELOOK_CATEGORY, () -> true, true));
	}
}
