package vertigo.aminorconvenience;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {

	private static final int BUTTON_WIDTH = 310;
	private static final int BUTTON_HEIGHT = 20;

	private final Screen parent;

	private boolean modified = false;

	protected ConfigScreen(Screen parent) {
		super(Text.literal("a-minor-convenience.options"));
		this.parent = parent;
	}

	@Override
	protected void init() {
		ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
		layout.addHeader(new TextWidget(Text.translatable("a-minor-convenience.text.optionsTitle"), this.textRenderer));
		GridWidget grid = new GridWidget();
		grid.setRowSpacing(5);
		GridWidget.Adder adder = grid.createAdder(1);
		adder.add(createToggleButton("swapFlowers", AMinorConvenience.CONFIG.swapFlowers, b -> {
			setToggleButtonMessage(b, "swapFlowers", AMinorConvenience.CONFIG.swapFlowers ^= true);
		}));
		adder.add(createToggleButton("cyclePaintings", AMinorConvenience.CONFIG.cyclePaintings, b -> {
			setToggleButtonMessage(b, "cyclePaintings", AMinorConvenience.CONFIG.cyclePaintings ^= true);
		}));
		adder.add(createToggleButton("seamlessCreativeFlight", AMinorConvenience.CONFIG.seamlessCreativeFlight, b -> {
			setToggleButtonMessage(b, "seamlessCreativeFlight", AMinorConvenience.CONFIG.seamlessCreativeFlight ^= true);
		}));
		adder.add(createToggleButton("enchantmentNames", AMinorConvenience.CONFIG.enchantmentNames, b -> {
			setToggleButtonMessage(b, "enchantmentNames", AMinorConvenience.CONFIG.enchantmentNames ^= true);
		}));
		layout.addBody(grid);
		layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, b -> {
			close();
		}).build());
		layout.forEachChild(this::addDrawableChild);
		layout.refreshPositions();
	}

	@Override
	public void close() {
		if (modified) {
			AMinorConvenience.CONFIG.write();
		}
		this.client.setScreen(this.parent);
	}

	private ButtonWidget createToggleButton(String key, boolean value, ButtonWidget.PressAction action) {
		return ButtonWidget.builder(ScreenTexts.composeToggleText(Text.translatable("a-minor-convenience.option." + key), value), action).tooltip(Tooltip.of(Text.translatable("a-minor-convenience.tooltip." + key))).size(BUTTON_WIDTH, BUTTON_HEIGHT).build();
	}

	private void setToggleButtonMessage(ButtonWidget button, String key, boolean value) {
		button.setMessage(ScreenTexts.composeToggleText(Text.translatable("a-minor-convenience.option." + key), value));
		modified = true;
	}

}