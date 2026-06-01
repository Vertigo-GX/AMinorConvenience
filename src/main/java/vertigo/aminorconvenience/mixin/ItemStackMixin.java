package vertigo.aminorconvenience.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vertigo.aminorconvenience.AMinorConvenience;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemInstance {

	@Inject(method = "getItemName", at = @At("HEAD"), cancellable = true)
	public void getItemNameInject(CallbackInfoReturnable<Component> info) {
		if(!AMinorConvenience.CONFIG.enchantmentNames || !this.is(Items.ENCHANTED_BOOK)) {
			return;
		}
		ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting((ItemStack) (Object) this);
		if(enchantments.size() != 1) {
			return;
		}
		Object2IntMap.Entry<Holder<Enchantment>> enchantment = enchantments.entrySet().iterator().next();
		info.setReturnValue(((MutableComponent) Enchantment.getFullname(enchantment.getKey(), enchantment.getIntValue())).setStyle(Style.EMPTY));
	}

}