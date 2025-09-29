package com.modrinth.methane.mixin;

import com.modrinth.methane.Methane;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    /**
     * @author AnOpenSauceDev
     * @reason what fog??!?!? never heard of it.
     */

//    @Inject(method = "Lnet/minecraft/client/render/fog/FogRenderer;applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;", at = @At(value="INVOKE",target="Lnet/minecraft/client/render/fog/FogRenderer;applyFog(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V",shift=At.Shift.BEFORE), cancellable = true)
//    private void applyFog(Camera camera, int viewDistance, boolean thick, RenderTickCounter tickCounter, float skyDarkness, ClientWorld world, CallbackInfoReturnable<Vector4f> cir) {
//        if (!Methane.ModActive && !Methane.settings.fogSettings.persistFogSettings) return;
//        if (hasBlindOrDark(camera.getFocusedEntity())) return;
//        fogData.renderDistanceStart=0;
//        fogData.renderDistanceEnd = 0;
//    }

    @ModifyArgs(method = "Lnet/minecraft/client/render/fog/FogRenderer;applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;",at=@At(value="INVOKE",target="Lnet/minecraft/client/render/fog/FogRenderer;applyFog(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V"))
    private void fogDelete(Args args) {
        if (!Methane.ModActive && !Methane.settings.fogSettings.persistFogSettings) return;
        if (Methane.settings.fogSettings.disableSkyFog) {
        args.set(6,99999f);
        args.set(5,99999f);}
        if (Methane.settings.fogSettings.disableEnvironmentalFog) {
        args.set(4,99999f);
        args.set(3,99999f);}
    }

    @Unique
    private static boolean hasBlindOrDark(Entity entity) {
        if (!(entity instanceof ClientPlayerEntity)) return false;
        return ((ClientPlayerEntity) entity).hasStatusEffect(StatusEffects.BLINDNESS)
                || ((ClientPlayerEntity) entity).hasStatusEffect(StatusEffects.DARKNESS);
    }
}
