package com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.mixin;

import com.github.mahmudindev.mcmod.modifiedbiomesource.core.ModifiedMultiNoiseBiomeSource;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import terrablender.worldgen.IExtendedParameterList;

@Mixin(ModifiedMultiNoiseBiomeSource.class)
public abstract class ModifiedMultiNoiseBiomeSourceMixin {
    @WrapOperation(
            method = "/^lambda\\$createResolver\\$.*/",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/github/mahmudindev/mcmod/modifiedbiomesource/core/ModifiedMultiNoiseBiomeSource;getNoiseBiome(Lnet/minecraft/world/level/biome/Climate$TargetPoint;)Lnet/minecraft/core/Holder;"
            )
    )
    private Holder<Biome> getNoiseBiome(
            ModifiedMultiNoiseBiomeSource source,
            Climate.TargetPoint target,
            Operation<Holder<Biome>> original,
            @Local(argsOnly = true, ordinal = 0) int x,
            @Local(argsOnly = true, ordinal = 1) int y,
            @Local(argsOnly = true, ordinal = 2) int z
    ) {
        ModifiedMultiNoiseBiomeSource biomeSource = (ModifiedMultiNoiseBiomeSource) (Object) this;
        IExtendedParameterList<Holder<Biome>> parameters = (IExtendedParameterList<Holder<Biome>>) biomeSource.getParameters();
        return parameters.isInitialized() ? parameters.findValuePositional(target, x, y, z) : original.call(source, target);
    }

    @WrapOperation(
            method = "/^lambda\\$createResolverForChunk\\$.*/",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/github/mahmudindev/mcmod/modifiedbiomesource/core/ModifiedMultiNoiseBiomeSource;getNoiseBiome(Lnet/minecraft/world/level/biome/Climate$TargetPoint;)Lnet/minecraft/core/Holder;"
            )
    )
    private Holder<Biome> getNoiseBiomeForChunk(
            ModifiedMultiNoiseBiomeSource source,
            Climate.TargetPoint target,
            Operation<Holder<Biome>> original,
            @Local(argsOnly = true, ordinal = 3) int x,
            @Local(argsOnly = true, ordinal = 4) int y,
            @Local(argsOnly = true, ordinal = 5) int z
    )
    {
        ModifiedMultiNoiseBiomeSource biomeSource = (ModifiedMultiNoiseBiomeSource) (Object) this;
        IExtendedParameterList<Holder<Biome>> parameters = (IExtendedParameterList<Holder<Biome>>) biomeSource.getParameters();
        return parameters.isInitialized() ? parameters.findValuePositional(target, x, y, z) : original.call(source, target);
    }
}
