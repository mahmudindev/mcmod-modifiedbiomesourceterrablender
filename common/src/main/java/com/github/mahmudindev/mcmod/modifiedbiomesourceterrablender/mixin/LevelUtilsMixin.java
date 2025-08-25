package com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.mixin;

import com.github.mahmudindev.mcmod.modifiedbiomesource.core.ModifiedMultiNoiseBiomeSource;
import com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.ModifiedBiomeSourceTerraBlender;
import com.github.mahmudindev.mcmod.modifiedbiomesourceterrablender.base.IClimateParameterList;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.util.LevelUtils;
import terrablender.worldgen.IExtendedBiomeSource;
import terrablender.worldgen.IExtendedNoiseGeneratorSettings;
import terrablender.worldgen.IExtendedParameterList;

@Mixin(LevelUtils.class)
public abstract class LevelUtilsMixin {
    @Inject(method = "initializeBiomes", at = @At(value = "RETURN", ordinal = 2))
    private static void initializeBiomesSupport(
            RegistryAccess registryAccess,
            Holder<DimensionType> dimensionType,
            ResourceKey<LevelStem> levelResourceKey,
            ChunkGenerator chunkGenerator,
            long seed,
            CallbackInfo ci
    ) {
        if (!(chunkGenerator instanceof NoiseBasedChunkGenerator chunkGeneratorX)) {
            return;
        }

        BiomeSource biomeSource = chunkGenerator.getBiomeSource();

        if (!(biomeSource instanceof ModifiedMultiNoiseBiomeSource)) {
            return;
        }

        RegionType regionType = LevelUtils.getRegionTypeForDimension(dimensionType);

        if (regionType == null) {
            return;
        }

        NoiseGeneratorSettings genSettings = chunkGeneratorX.generatorSettings().value();
        SurfaceRuleManager.RuleCategory surfaceRule = switch(regionType) {
            case OVERWORLD -> SurfaceRuleManager.RuleCategory.OVERWORLD;
            case NETHER -> SurfaceRuleManager.RuleCategory.NETHER;
            default -> throw new IllegalArgumentException(String.format(
                    "Attempted to get surface rule category for unsupported region type %s",
                    regionType
            ));
        };
        ((IExtendedNoiseGeneratorSettings) (Object) genSettings).setRuleCategory(surfaceRule);

        ModifiedMultiNoiseBiomeSource biomeSrc = (ModifiedMultiNoiseBiomeSource) biomeSource;
        Climate.ParameterList<Holder<Biome>> biomeParams = biomeSrc.getParameters();
        ((IClimateParameterList<?>) biomeParams).modifiedbiomesource$setBiomeSource(biomeSrc);
        ((IExtendedParameterList<?>) biomeParams).initializeForTerraBlender(
                registryAccess,
                regionType,
                seed
        );

        Registry<Biome> biomeRegistry = registryAccess.lookupOrThrow(Registries.BIOME);
        ImmutableList.Builder<Holder<Biome>> biomeBuilder = ImmutableList.builder();
        if (biomeSrc.isModSupported()) {
            Regions.get(regionType).forEach(region -> {
                region.addBiomes(biomeRegistry, pair -> {
                    ResourceKey<Biome> biome = pair.getSecond();

                    if (biomeRegistry.containsKey(biome)) {
                        Holder<Biome> biomeX = biomeRegistry.getOrThrow(biome);

                        if (!biomeSrc.canGenerate(biomeX)) {
                            return;
                        }

                        biomeBuilder.add(biomeX);
                    }
                });
            });
        }
        ((IExtendedBiomeSource) biomeSrc).appendDeferredBiomesList(biomeBuilder.build());

        ModifiedBiomeSourceTerraBlender.LOGGER.info(
                "Initialized TerraBlender biomes for level stem {}",
                levelResourceKey.location()
        );
    }
}
