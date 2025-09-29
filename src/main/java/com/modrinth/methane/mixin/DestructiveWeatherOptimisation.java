package com.modrinth.methane.mixin;


import com.modrinth.methane.Methane;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class DestructiveWeatherOptimisation { // Dragons Beware!
    @Inject(method = "renderWeather", at = @At("HEAD"),cancellable = true)
    public void biomeHacks(FrameGraphBuilder frameGraphBuilder, Vec3d cameraPos, float tickProgress, GpuBufferSlice fog, CallbackInfo ci){
        if(Methane.ModActive && Methane.settings.destructiveSettings.destructiveweatheroptimizations) ci.cancel();
    }
}
