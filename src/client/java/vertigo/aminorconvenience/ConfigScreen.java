package vertigo.aminorconvenience;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {

	private static final int BUTTON_WIDTH = 310;

	private static final int BUTTON_HEIGHT = 20;

	private final Screen parent;

	private boolean modified = false;

	protected ConfigScreen(Screen parent) {
		super(Component.literal("a-minor-convenience.options"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
		layout.addToHeader(new StringWidget(Component.translatable("a-minor-convenience.text.optionsTitle"), this.font));
		GridLayout grid = new GridLayout();
		grid.rowSpacing(5);
		GridLayout.RowHelper adder = grid.createRowHelper(1);
		adder.addChild(createToggleButton("swapFlowers", AMinorConvenience.CONFIG.swapFlowers, b -> setToggleButtonMessage(b, "swapFlowers", AMinorConvenience.CONFIG.swapFlowers ^= true)));
		adder.addChild(createToggleButton("cyclePaintings", AMinorConvenience.CONFIG.cyclePaintings, b -> setToggleButtonMessage(b, "cyclePaintings", AMinorConvenience.CONFIG.cyclePaintings ^= true)));
		adder.addChild(createToggleButton("seamlessCreativeFlight", AMinorConvenience.CONFIG.seamlessCreativeFlight, b -> setToggleButtonMessage(b, "seamlessCreativeFlight", AMinorConvenience.CONFIG.seamlessCreativeFlight ^= true)));
		adder.addChild(createToggleButton("enchantmentNames", AMinorConvenience.CONFIG.enchantmentNames, b -> setToggleButtonMessage(b, "enchantmentNames", AMinorConvenience.CONFIG.enchantmentNames ^= true)));
		layout.addToContents(grid);
		layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, b -> {
			onClose();
		}).build());
		layout.visitWidgets(this::addRenderableWidget);
		layout.arrangeElements();
	}

	@Override
	public void onClose() {
		if(modified) {
			AMinorConvenience.CONFIG.write();
		}
		this.minecraft.setScreen(this.parent);
	}

	private Button createToggleButton(String key, boolean value, Button.OnPress action) {
		return Button.builder(CommonComponents.optionStatus(Component.translatable("a-minor-convenience.option." + key), value), action).tooltip(Tooltip.create(Component.translatable("a-minor-convenience.tooltip." + key))).size(BUTTON_WIDTH, BUTTON_HEIGHT).build();
	}

	private void setToggleButtonMessage(Button button, String key, boolean value) {
		button.setMessage(CommonComponents.optionStatus(Component.translatable("a-minor-convenience.option." + key), value));
		modified = true;
	}

}