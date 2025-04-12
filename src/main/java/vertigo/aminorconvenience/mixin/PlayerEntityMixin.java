package vertigo.aminorconvenience.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vertigo.aminorconvenience.AMinorConvenience;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow
	@Final
	private PlayerAbilities abilities;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public boolean isOnGround() {
		return AMinorConvenience.CONFIG.seamlessCreativeFlight && this.abilities.creativeMode && this.abilities.flying ? false : super.isOnGround();
	}

}