package vertigo.aminorconvenience.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vertigo.aminorconvenience.AMinorConvenience;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

	@Shadow
	@Final
	private Abilities abilities;

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public boolean onGround() {
		return AMinorConvenience.CONFIG.seamlessCreativeFlight && this.abilities.instabuild && this.abilities.flying ? false : super.onGround();
	}

}