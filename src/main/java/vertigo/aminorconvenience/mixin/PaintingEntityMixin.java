package vertigo.aminorconvenience.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.PaintingVariantTags;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vertigo.aminorconvenience.AMinorConvenience;

import java.util.ArrayList;

@Mixin(PaintingEntity.class)
public abstract class PaintingEntityMixin extends Entity {

	@Shadow
	public abstract void setVariant(RegistryEntry<PaintingVariant> variant);

	@Shadow
	public abstract RegistryEntry<PaintingVariant> getVariant();

	public PaintingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (getWorld().isClient || !AMinorConvenience.CONFIG.cyclePaintings || !player.getStackInHand(hand).isOf(Items.PAINTING)) {
			return ActionResult.PASS;
		}
		ArrayList<RegistryEntry<PaintingVariant>> variants = new ArrayList<>();
		RegistryEntry<PaintingVariant> variant = this.getVariant();
		PaintingVariant value = variant.value();
		int height = value.height();
		int width = value.width();
		for (RegistryEntry<PaintingVariant> v : this.getWorld().getRegistryManager().getOrThrow(RegistryKeys.PAINTING_VARIANT).iterateEntries(PaintingVariantTags.PLACEABLE)) {
			PaintingVariant current = v.value();
			if (current.height() != height || current.width() != width) {
				continue;
			}
			variants.add(v);
		}
		int size = variants.size();
		if (size < 2) {
			return ActionResult.FAIL;
		}
		int index = variants.indexOf(variant);
		size--;
		if (player.isSneaking()) {
			index -= index == 0 ? -size : 1;
		} else {
			index += index == size ? -size : 1;
		}
		this.setVariant(variants.get(index));
		player.incrementStat(Stats.USED.getOrCreateStat(Items.PAINTING));
		return ActionResult.SUCCESS;
	}

}