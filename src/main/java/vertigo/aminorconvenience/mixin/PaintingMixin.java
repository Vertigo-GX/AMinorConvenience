package vertigo.aminorconvenience.mixin;

import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vertigo.aminorconvenience.AMinorConvenience;

import java.util.ArrayList;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.stats.Stats;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@Mixin(Painting.class)
public abstract class PaintingMixin extends Entity {

	@Shadow
	protected abstract void setVariant(Holder<PaintingVariant> variant);

	@Shadow
	public abstract Holder<PaintingVariant> getVariant();

	public PaintingMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public @NonNull InteractionResult interact(@NonNull Player player, @NonNull InteractionHand hand) {
		if(this.level().isClientSide() || !AMinorConvenience.CONFIG.cyclePaintings || !player.getItemInHand(hand).is(Items.PAINTING)) {
			return InteractionResult.PASS;
		}
		ArrayList<Holder<PaintingVariant>> variants = new ArrayList<>();
		Holder<PaintingVariant> variant = getVariant();
		PaintingVariant value = variant.value();
		int height = value.height();
		int width = value.width();
		for(Holder<PaintingVariant> v : this.level().registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(PaintingVariantTags.PLACEABLE)) {
			PaintingVariant current = v.value();
			if(current.height() != height || current.width() != width) {
				continue;
			}
			variants.add(v);
		}
		int size = variants.size();
		if(size < 2) {
			return InteractionResult.FAIL;
		}
		int index = variants.indexOf(variant);
		size--;
		if(player.isShiftKeyDown()) {
			index -= index == 0 ? -size : 1;
		} else {
			index += index == size ? -size : 1;
		}
		setVariant(variants.get(index));
		player.awardStat(Stats.ITEM_USED.get(Items.PAINTING));
		return InteractionResult.SUCCESS;
	}

}