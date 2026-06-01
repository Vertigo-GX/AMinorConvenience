package vertigo.aminorconvenience.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vertigo.aminorconvenience.AMinorConvenience;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin {

	@Shadow
	@Final
	private static Map<Block, Block> POTTED_BY_CONTENT;

	@Shadow
	@Final
	private Block potted;

	@Shadow
	protected abstract boolean isEmpty();

	@Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
	protected void useItemOnInject(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> info) {
		if(world.isClientSide() || !AMinorConvenience.CONFIG.swapFlowers || isEmpty()) {
			return;
		}
		if(!(stack.getItem() instanceof BlockItem item)) {
			return;
		}
		Block block = item.getBlock();
		if(block.equals(this.potted)) {
			return;
		}
		Block potted = POTTED_BY_CONTENT.get(block);
		if(potted == null) {
			return;
		}
		ItemStack content = new ItemStack(this.potted);
		if(!player.addItem(content)) {
			player.drop(content, false);
		}
		world.setBlock(pos, potted.defaultBlockState(), 3);
		world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		player.awardStat(Stats.POT_FLOWER);
		stack.consume(1, player);
		info.setReturnValue(InteractionResult.SUCCESS);
	}

}