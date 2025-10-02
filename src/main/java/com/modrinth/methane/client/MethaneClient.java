package com.modrinth.methane.client;

import com.modrinth.methane.Methane;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MethaneClient implements ClientModInitializer {

    public KeyBinding MethaneToggle;

    public int REBUILD_TICKS_THRESHOLD = Methane.settings.rebuildSeconds * 20; // rebuild every x seconds
    static int ticks = 0;



    @Override
    public void onInitializeClient() {

        Methane.isClient = true;*

        Methane.ModActive = Methane.settings.modstate;

        // this causes us to need the Fabric API.
        MethaneToggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                        "key.methane.toggle",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_UNKNOWN,
                        "category.methane.keys"
                )
        );


        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            ticks++;


            if (Methane.ModActive && Methane.settings.dynamicShading && client.player != null) {
                if (ticks > REBUILD_TICKS_THRESHOLD) {
                    BrightnessUtil.rebuildChunks(client); // has a *very* tiny performance impact, that only happens once every 20 seconds. It's also multithreaded!
                    ticks = 0;
                }
            }


            if (client.player == null) { // I'm assuming that ClientPlayerEntity is only ever null if you quit the server.
                Methane.ServerForbidsChanging = false;

            } else if (Methane.playerBlockingPacket) { // I wanted to avoid this at all costs, but it looks like I have to do this hack.

                Methane.playerBlockingPacket = false;
                //ClientPlayNetworking.send(METHANE_RESP_PACKET, PacketByteBufs.empty());

            }


            while (MethaneToggle.wasPressed()) {

                ToggleMethane(client, false);

            }
        });


    }

    public static void ToggleMethane(MinecraftClient client, boolean force) {
        if (!Methane.ServerForbidsChanging || force) {


            Methane.ModActive = !Methane.ModActive;

            Methane.MethaneDebugger.Log("Methane state toggled to: " + Methane.ModActive);

            if (Methane.settings.hudrender) {

                if (Methane.ModActive) {

                    client.player.sendMessage(Text.translatable("methane.active"), true);

                } else {

                    client.player.sendMessage(Text.translatable("methane.offline"), true);
                }


            } else {
                if (Methane.ModActive) {

                    client.player.sendMessage(Text.translatable("Methane activated!"), true);

                } else {

                    client.player.sendMessage(Text.translatable("Methane disabled!"), true);
                }
            }
        }
    }

    public static void ToggleMethaneSetBool(MinecraftClient client, boolean state) {
        Methane.ModActive = state;
        if (client.player == null) return;
        if (Methane.settings.hudrender) {
            if (Methane.ModActive) {
                client.player.sendMessage(Text.translatable("methane.active"), true);
            } else {
                client.player.sendMessage(Text.translatable("methane.offline"), true);
            }
        }
    }
}