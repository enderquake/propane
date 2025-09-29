package com.modrinth.methane.mixin;

import com.modrinth.methane.Methane;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class,priority = 1000)
public class WorldRendererMixin {

    @Inject(method = "renderWeather", at = @At("HEAD"),cancellable = true)
    public void delWeather(FrameGraphBuilder frameGraphBuilder, Vec3d cameraPos, float tickProgress, GpuBufferSlice fog, CallbackInfo ci){
        if(Methane.settings.destructiveSettings.DestroyWeather)
            ci.cancel();
    }
}
