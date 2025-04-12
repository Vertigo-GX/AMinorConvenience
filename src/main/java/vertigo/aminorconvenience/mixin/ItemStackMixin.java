package vertigo.aminorconvenience.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vertigo.aminorconvenience.AMinorConvenience;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder {

	@Shadow
	public abstract boolean isOf(Item item);

	@Inject(method = "getItemName", at = @At("HEAD"), cancellable = true)
	public void getItemName(CallbackInfoReturnable<Text> info) {
		if (!AMinorConvenience.CONFIG.enchantmentNames || !this.isOf(Items.ENCHANTED_BOOK)) {
			return;
		}
		ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments((ItemStack) (Object) this);
		if (enchantments.getSize() != 1) {
			return;
		}
		Object2IntMap.Entry<RegistryEntry<Enchantment>> enchantment = enchantments.getEnchantmentEntries().iterator().next();
		info.setReturnValue(((MutableText) Enchantment.getName(enchantment.getKey(), enchantment.getIntValue())).setStyle(Style.EMPTY));
	}

}