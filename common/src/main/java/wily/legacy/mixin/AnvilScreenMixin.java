package wily.legacy.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import wily.legacy.client.LegacySprites;
import wily.legacy.util.ScreenUtil;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {

    @Shadow private EditBox name;

    @Shadow protected abstract void onNameChanged(String string);

    @Shadow @Final private static Component TOO_EXPENSIVE_TEXT;

    @Shadow @Final private Player player;

    @Override
    public void init() {
        imageWidth = 207;
        imageHeight = 215;
        inventoryLabelX = 10;
        inventoryLabelY = 105;
        titleLabelX = 73;
        titleLabelY = 11;
        super.init();
    }
    public AnvilScreenMixin(AnvilMenu itemCombinerMenu, Inventory inventory, Component component, ResourceLocation resourceLocation) {
        super(itemCombinerMenu, inventory, component, resourceLocation);
    }
    public void renderLabels(GuiGraphics guiGraphics, int i, int j) {
        super.renderLabels(guiGraphics, i, j);
        int k = this.menu.getCost();
        if (k > 0) {
            Component component;
            int l = 8453920;
            if (k >= 40 && !this.minecraft.player.getAbilities().instabuild) {
                component = TOO_EXPENSIVE_TEXT;
                l = 0xFF6060;
            } else if (!this.menu.getSlot(2).hasItem()) {
                component = null;
            } else {
                component = Component.translatable("container.repair.cost", k);
                if (!this.menu.getSlot(2).mayPickup(this.player)) {
                    l = 0xFF6060;
                }
            }
            if (component != null) {
                int m = this.imageWidth - 8 - this.font.width(component) - 2;
                guiGraphics.drawString(this.font, component, m, 90, l);
            }
        }
    }
    public void subInit() {
        this.name = new EditBox(this.font, leftPos + 72, topPos + 26, 120, 18, Component.translatable("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setMaxLength(50);
        this.name.setResponder(this::onNameChanged);
        this.name.setValue("");
        this.addWidget(this.name);
        this.setInitialFocus(this.name);
        this.name.setEditable(this.menu.getSlot(0).hasItem());
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        renderBg(guiGraphics, f, i, j);
    }

    @Override
    public void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        ScreenUtil.renderPanel(guiGraphics,leftPos,topPos,imageWidth,imageHeight,2f);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(leftPos + 13.5, topPos + 9.5,0f);
        guiGraphics.pose().scale(2.5f,2.5f,2.5f);
        guiGraphics.blitSprite(LegacySprites.ANVIL_HAMMER_SPRITE,0,0,15,15);
        guiGraphics.pose().popPose();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(leftPos + 53, topPos + 60,0f);
        guiGraphics.pose().scale(1.5f,1.5f,1.5f);
        guiGraphics.blitSprite(LegacySprites.COMBINER_PLUS_SPRITE,0,0,13,13);
        guiGraphics.pose().popPose();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(leftPos + 122, topPos + 59,0f);
        guiGraphics.pose().scale(1.5f,1.5f,1.5f);
        guiGraphics.blitSprite(LegacySprites.ARROW_SPRITE,0,0,22,15);
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem())
            guiGraphics.blitSprite(LegacySprites.ERROR_CROSS_SPRITE, 4, 0, 15, 15);
        guiGraphics.pose().popPose();
    }
}
