package com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.mixin;

import com.github.mahmudindev.mcmod.modifiedbiomesource.core.ModifiedMultiNoiseBiomeSource;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import terrablender.worldgen.IExtendedParameterList;

@Mixin(ModifiedMultiNoiseBiomeSource.class)
public abstract class ModifiedMultiNoiseBiomeSourceMixin {
    @Inject(
            method = "method_38109(IIILnet/minecraft/world/level/biome/Climate$Sampler;)Lnet/minecraft/core/Holder;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getNoiseBiomeSupport(
            int x,
            int y,
            int z,
            Climate.Sampler sampler,
            CallbackInfoReturnable<Holder<Biome>> cir
    ) {
        ModifiedMultiNoiseBiomeSource biomeSource = (ModifiedMultiNoiseBiomeSource) (Object) this;
        Climate.ParameterList<Holder<Biome>> parameters = biomeSource.getParameters();
        cir.setReturnValue(((IExtendedParameterList<Holder<Biome>>) parameters).findValuePositional(
                sampler.sample(x, y, z),
                x,
                y,
                z
        ));
    }
}
