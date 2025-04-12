package vertigo.aminorconvenience.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vertigo.aminorconvenience.AMinorConvenience;

import java.util.Map;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin {

	@Shadow
	@Final
	private static Map<Block, Block> CONTENT_TO_POTTED;

	@Shadow
	@Final
	private Block content;

	@Shadow
	protected abstract boolean isEmpty();

	@Inject(method = "onUseWithItem", at = @At("HEAD"), cancellable = true)
	protected void onUseWithItemInject(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> info) {
		if (world.isClient || !AMinorConvenience.CONFIG.swapFlowers || this.isEmpty()) {
			return;
		}
		if (stack.getItem() instanceof BlockItem i) {
			Block block = i.getBlock();
			if (block.equals(this.content)) {
				return;
			}
			Block potted = CONTENT_TO_POTTED.get(block);
			if (potted == null) {
				return;
			}
			ItemStack content = new ItemStack(this.content);
			if (!player.giveItemStack(content)) {
				player.dropItem(content, false);
			}
			world.setBlockState(pos, potted.getDefaultState(), 3);
			world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			player.incrementStat(Stats.POT_FLOWER);
			stack.decrementUnlessCreative(1, player);
			info.setReturnValue(ActionResult.SUCCESS);
		}
	}

}