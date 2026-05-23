package com.pixelstorm.freelook_for_clients;

import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;

public class FreelookForClients implements ClientModInitializer {

	private static final KeyBinding.Category FREELOOK_CATEGORY = KeyBinding.Category.create(Identifier.of("freelook_for_clients"));

	public static KeyBinding holdFreeLookKeybind;

	public static KeyBinding toggleFreeLookKeybind;

	@Override
	public void onInitializeClient() {
		holdFreeLookKeybind = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.freelook_for_clients.hold", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_WORLD_1,
				FREELOOK_CATEGORY));
		toggleFreeLookKeybind = KeyBindingHelper.registerKeyBinding(
			new StickyKeyBinding("key.freelook_for_clients.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				FREELOOK_CATEGORY, () -> true, true));
	}
}
