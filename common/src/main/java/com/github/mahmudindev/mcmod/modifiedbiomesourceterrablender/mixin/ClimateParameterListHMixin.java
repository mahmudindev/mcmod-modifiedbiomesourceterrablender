package com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.mahmudindev.mcmod.modifiedbiomesource.core.IModifiedBiomeSource;
import com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.base.IClimateParameterList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import terrablender.api.Region;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Mixin(value = Climate.ParameterList.class, priority = 1500)
public abstract class ClimateParameterListHMixin<T> implements IClimateParameterList<T> {
    @Unique
    private BiomeSource biomeSource;

    @TargetHandler(
            mixin = "terrablender.mixin.MixinParameterList",
            name = "initializeForTerraBlender"
    )
    @WrapOperation(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lterrablender/api/Region;addBiomes(Lnet/minecraft/core/Registry;Ljava/util/function/Consumer;)V"
            )
    )
    private void initializeForTerraBlenderSupport(
            Region instance,
            Registry<Biome> registry,
            Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper,
            Operation<Void> original
    ) {
        if (!(this.biomeSource instanceof IModifiedBiomeSource biomeSourceX)) {
            original.call(instance, registry, mapper);
            return;
        }

        AtomicInteger i = new AtomicInteger();

        Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapperX = pair -> {
            if (!biomeSourceX.isModSupported()) {
                mapper.accept(pair);
                return;
            }

            if (biomeSourceX.canGenerate(registry.getHolderOrThrow(pair.getSecond()))) {
                mapper.accept(pair);
                i.getAndIncrement();
            }
        };

        original.call(instance, registry, mapperX);

        if (i.get() < 1) {
            mapper.accept(new Pair<>(
                    Climate.parameters(0, 0, 0, 0, 0, 0, 0),
                    Region.DEFERRED_PLACEHOLDER
            ));
        }
    }

    @Override
    public void modifiedbiomesource$setBiomeSource(BiomeSource biomeSource) {
        this.biomeSource = biomeSource;
    }
}
