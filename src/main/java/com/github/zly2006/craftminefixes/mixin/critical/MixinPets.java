package com.github.zly2006.craftminefixes.mixin.critical;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.pets.PetBee;
import net.minecraft.world.entity.pets.PetWolf;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = {PetWolf.class, PetBee.class})
public abstract class MixinPets implements NeutralMob {
    @Override
    public boolean isAngryAtAllPlayers(ServerLevel serverLevel) {
        return false;
    }
}
