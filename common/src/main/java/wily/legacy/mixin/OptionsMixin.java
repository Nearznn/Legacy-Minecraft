package wily.legacy.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wily.legacy.Legacy4JClient;
import wily.legacy.client.LegacyOptions;
import wily.legacy.client.controller.ControllerComponent;
import wily.legacy.client.controller.LegacyKeyMapping;
import wily.legacy.client.screen.ControlTooltip;

import java.io.File;
import java.util.Arrays;
import java.util.function.BooleanSupplier;

@Mixin(Options.class)
public abstract class OptionsMixin implements LegacyOptions {
    @Shadow public boolean hideGui;

    @Shadow protected Minecraft minecraft;

    @Shadow public abstract void load();

    @Shadow
    public static Component genericValueLabel(Component arg, Component arg2) {
        return null;
    }

    @Shadow
    public static Component genericValueLabel(Component arg, int i) {
        return null;
    }

    @Shadow
    protected static Component percentValueLabel(Component component, double d) {
        return null;
    }

    @Shadow @Final public KeyMapping[] keyMappings;

    @Shadow public abstract OptionInstance<Integer> renderDistance();

    @Shadow public abstract OptionInstance<Boolean> bobView();

    private OptionInstance<Double> hudDistance;
    private OptionInstance<Double> hudOpacity;
    private OptionInstance<Double> interfaceResolution;
    private OptionInstance<Double> interfaceSensitivity;
    private OptionInstance<Integer> terrainFogStart;
    private OptionInstance<Double> terrainFogEnd;
    private OptionInstance<Integer> autoSaveInterval;
    private OptionInstance<Boolean> overrideTerrainFogStart;
    private OptionInstance<Boolean> legacyCreativeTab;
    private OptionInstance<Boolean> displayHUD;
    private OptionInstance<Boolean> animatedCharacter;
    private OptionInstance<Boolean> classicCrafting;
    private OptionInstance<Boolean> vanillaTabs;
    private OptionInstance<Boolean> autoSaveWhenPause;
    private OptionInstance<Integer> hudScale;
    private OptionInstance<Boolean> showVanillaRecipeBook;
    private OptionInstance<Double> legacyGamma;
    private OptionInstance<Boolean> inGameTooltips;
    private OptionInstance<Boolean> tooltipBoxes;
    private OptionInstance<Boolean> hints;
    private OptionInstance<Boolean> directSaveLoad;
    private OptionInstance<Boolean> vignette;
    private OptionInstance<Boolean> forceYellowText;
    private OptionInstance<Boolean> displayNameTagBorder;
    private OptionInstance<Boolean> legacyItemTooltips;
    private OptionInstance<Boolean> caveSounds;
    private OptionInstance<Boolean> minecartSounds;
    private OptionInstance<Boolean> invertYController;
    private OptionInstance<Boolean> invertControllerButtons;
    private OptionInstance<Integer> selectedController;
    private OptionInstance<Integer> controllerIcons;
    private OptionInstance<Difficulty> createWorldDifficulty;

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;<init>(Ljava/lang/String;ILjava/lang/String;)V", ordinal = 5),index = 0)
    protected String initKeyCraftingName(String string) {
        return "legacy.key.inventory";
    }
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;<init>(Ljava/lang/String;ILjava/lang/String;)V", ordinal = 5),index = 1)
    protected int initKeyCrafting(int i) {
        return 73;
    }
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ToggleKeyMapping;<init>(Ljava/lang/String;ILjava/lang/String;Ljava/util/function/BooleanSupplier;)V", ordinal = 0),index = 3)
    protected BooleanSupplier initKeyShift(BooleanSupplier booleanSupplier) {
        return ()-> (minecraft == null || minecraft.player == null || (!minecraft.player.getAbilities().flying && minecraft.player.getVehicle() == null && !minecraft.player.isInWater() )) && booleanSupplier.getAsBoolean();
    }
    @Redirect(method = "<init>", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/Options;load()V"))
    protected void init(Options instance) {

    }
    @Inject(method = "<init>", at = @At( "RETURN"))
    protected void init(Minecraft minecraft, File file, CallbackInfo ci) {
        animatedCharacter = OptionInstance.createBoolean("legacy.options.animatedCharacter",true);
        classicCrafting = OptionInstance.createBoolean("legacy.options.classicCrafting",false);
        vanillaTabs = OptionInstance.createBoolean("legacy.options.vanillaTabs",OptionInstance.cachedConstantTooltip(Component.translatable("legacy.options.vanillaTabs.description")),false);
        legacyGamma = new OptionInstance<>("legacy.options.gamma", OptionInstance.noTooltip(), OptionsMixin::percentValueLabel, OptionInstance.UnitDouble.INSTANCE, 0.5, d -> {});
        displayHUD = OptionInstance.createBoolean("legacy.options.displayHud",!hideGui, b-> hideGui = !b);
        legacyCreativeTab = OptionInstance.createBoolean("legacy.options.creativeTab", true);
        autoSaveWhenPause = OptionInstance.createBoolean("legacy.options.autoSaveWhenPause", false);
        inGameTooltips = OptionInstance.createBoolean("legacy.options.gameTooltips", true);
        tooltipBoxes = OptionInstance.createBoolean("legacy.options.tooltipBoxes", true);
        hints = OptionInstance.createBoolean("legacy.options.hints", true);
        directSaveLoad = OptionInstance.createBoolean("legacy.options.directSaveLoad", false);
        vignette = OptionInstance.createBoolean("legacy.options.vignette", false);
        minecartSounds = OptionInstance.createBoolean("legacy.options.minecartSounds", true);
        caveSounds = OptionInstance.createBoolean("legacy.options.caveSounds", true);
        autoSaveInterval = new OptionInstance<>("legacy.options.autoSaveInterval", OptionInstance.noTooltip(), (c,i)-> i == 0 ? genericValueLabel(c,Component.translatable("options.off")) :Component.translatable( "legacy.options.mins_value",c, i * 5), new OptionInstance.IntRange(0,24), 1, d -> {});
        showVanillaRecipeBook = OptionInstance.createBoolean("legacy.options.showVanillaRecipeBook", false);
        forceYellowText = OptionInstance.createBoolean("legacy.options.forceYellowText", false);
        displayNameTagBorder = OptionInstance.createBoolean("legacy.options.displayNameTagBorder", true);
        legacyItemTooltips = OptionInstance.createBoolean("legacy.options.legacyItemTooltips", true);
        invertYController = OptionInstance.createBoolean("legacy.options.invertYController", false);
        invertControllerButtons = OptionInstance.createBoolean("legacy.options.invertControllerButtons", false, (b)-> ControllerComponent.RIGHT_BUTTON.componentState.block());
        selectedController = new OptionInstance<>("legacy.controls.controller", OptionInstance.noTooltip(), (c, i)-> Component.translatable("options.generic_value",c,Component.literal(i+1 + (Legacy4JClient.controllerHandler.connectedController == null ? "" : " (%s)".formatted(Legacy4JClient.controllerHandler.connectedController)))),  new OptionInstance.IntRange(0, 15), 0, d -> {});
        hudScale = new OptionInstance<>("legacy.options.hudScale", OptionInstance.noTooltip(), OptionsMixin::genericValueLabel,  new OptionInstance.IntRange(1,3), 2, d -> {});
        hudOpacity = new OptionInstance<>("legacy.options.hudOpacity", OptionInstance.noTooltip(), OptionsMixin::percentValueLabel, OptionInstance.UnitDouble.INSTANCE, 0.8, d -> {});
        hudDistance = new OptionInstance<>("legacy.options.hudDistance", OptionInstance.noTooltip(), OptionsMixin::percentValueLabel, OptionInstance.UnitDouble.INSTANCE, 1.0, d -> {});
        interfaceResolution = new OptionInstance<>("legacy.options.interfaceResolution", OptionInstance.noTooltip(), (c, d) -> percentValueLabel(c, 0.25 + d * 1.5), OptionInstance.UnitDouble.INSTANCE, 0.5, d -> minecraft.resizeDisplay());
        interfaceSensitivity = new OptionInstance<>("legacy.options.interfaceSensitivity", OptionInstance.noTooltip(), (c, d) -> percentValueLabel(c, d*2), OptionInstance.UnitDouble.INSTANCE, 0.5, d -> {});
        overrideTerrainFogStart = OptionInstance.createBoolean("legacy.options.overrideTerrainFogStart", true);
        terrainFogStart = new OptionInstance<>("legacy.options.terrainFogStart", OptionInstance.noTooltip(),(c,i)-> Component.translatable("options.chunks", i), new OptionInstance.ClampingLazyMaxIntRange(2, () -> renderDistance().get(), 0x7FFFFFFE), 4, d -> {});
        terrainFogEnd = new OptionInstance<>("legacy.options.terrainFogEnd", OptionInstance.noTooltip(),(c, d) -> percentValueLabel(c, d*2), OptionInstance.UnitDouble.INSTANCE, 0.5, d -> {});
        controllerIcons = new OptionInstance<>("legacy.options.controllerIcons", OptionInstance.noTooltip(), (c, i)-> Component.translatable("options.generic_value",c,i == 0? Component.translatable("legacy.options.auto_value", ControlTooltip.getActiveControllerType().displayName) : ControlTooltip.Type.values()[i].displayName),  new OptionInstance.IntRange(0, ControlTooltip.Type.values().length - 1), 0, d -> {});
        createWorldDifficulty = new OptionInstance<>("options.difficulty", d->Tooltip.create(d.getInfo()), (c, d) -> d.getDisplayName(), new OptionInstance.Enum<>(Arrays.asList(Difficulty.values()), Codec.INT.xmap(Difficulty::byId, Difficulty::getId)), Difficulty.NORMAL, d -> {});
        if(Legacy4JClient.canLoadVanillaOptions)
            load();
    }
    @Inject(method = "processOptions",at = @At("HEAD"))
    private void processOptions(Options.FieldAccess fieldAccess, CallbackInfo ci){
        fieldAccess.process("hudDistance", hudDistance);
        fieldAccess.process("hudOpacity", hudOpacity);
        fieldAccess.process("interfaceResolution", interfaceResolution);
        fieldAccess.process("interfaceSensitivity", interfaceSensitivity);
        fieldAccess.process("terrainFogStart", terrainFogStart);
        fieldAccess.process("terrainFogEnd", terrainFogEnd);
        fieldAccess.process("overrideTerrainFogStart", overrideTerrainFogStart);
        fieldAccess.process("autoSaveWhenPause", autoSaveWhenPause);
        fieldAccess.process("gameTooltips", inGameTooltips);
        fieldAccess.process("tooltipBoxes", tooltipBoxes);
        fieldAccess.process("hints", hints);
        fieldAccess.process("directSaveLoad", directSaveLoad);
        fieldAccess.process("vignette", vignette);
        fieldAccess.process("caveSounds", caveSounds);
        fieldAccess.process("minecartSounds", minecartSounds);
        fieldAccess.process("createWorldDifficulty", createWorldDifficulty);
        fieldAccess.process("autoSaveInterval", autoSaveInterval);
        fieldAccess.process("showVanillaRecipeBook", showVanillaRecipeBook);
        fieldAccess.process("forceYellowText", forceYellowText);
        fieldAccess.process("displayNameTagBorder", displayNameTagBorder);
        fieldAccess.process("legacyItemTooltips", legacyItemTooltips);
        fieldAccess.process("displayHUD", displayHUD);
        fieldAccess.process("invertYController", invertYController);
        fieldAccess.process("invertControllerButtons", invertControllerButtons);
        fieldAccess.process("selectedController", selectedController);
        fieldAccess.process("hudScale", hudScale);
        fieldAccess.process("controllerIcons", controllerIcons);
        fieldAccess.process("legacyCreativeTab", legacyCreativeTab);
        fieldAccess.process("animatedCharacter", animatedCharacter);
        fieldAccess.process("classicCrafting", classicCrafting);
        fieldAccess.process("vanillaTabs", vanillaTabs);
        fieldAccess.process("legacyGamma", legacyGamma);
        hideGui = !displayHUD.get();
        for (KeyMapping keyMapping : keyMappings) {
            LegacyKeyMapping mapping = (LegacyKeyMapping) keyMapping;
            int i = fieldAccess.process("component_" + keyMapping.getName(), mapping.getComponent() == null ? -1 : mapping.getComponent().ordinal());
            if ((mapping.getComponent() == null && i >= 0) || (mapping.getComponent() != null && mapping.getComponent().ordinal() != i))
                mapping.setComponent(i < 0 ? null : ControllerComponent.values()[i]);
        }
    }

    public OptionInstance<Double> hudDistance() {return hudDistance;}
    public OptionInstance<Double> hudOpacity() {return hudOpacity;}
    public OptionInstance<Double> interfaceResolution() {return interfaceResolution;}
    public OptionInstance<Double> interfaceSensitivity() {
        return interfaceSensitivity;
    }
    public OptionInstance<Boolean> overrideTerrainFogStart() {
        return overrideTerrainFogStart;
    }
    public OptionInstance<Integer> terrainFogStart() {
        return terrainFogStart;
    }
    public OptionInstance<Double> terrainFogEnd() {
        return terrainFogEnd;
    }
    public OptionInstance<Boolean> legacyCreativeTab() {
        return legacyCreativeTab;
    }
    public OptionInstance<Boolean> displayHUD() {
        return displayHUD;
    }
    public OptionInstance<Boolean> animatedCharacter() {
        return animatedCharacter;
    }
    public OptionInstance<Boolean> classicCrafting() {return classicCrafting;}
    public OptionInstance<Boolean> autoSaveWhenPause() {return autoSaveWhenPause;}
    public OptionInstance<Integer> autoSaveInterval() {
        return autoSaveInterval;
    }
    public OptionInstance<Boolean> showVanillaRecipeBook() {
        return showVanillaRecipeBook;
    }
    public OptionInstance<Double> legacyGamma() {return legacyGamma;}
    public OptionInstance<Boolean> inGameTooltips() {
        return inGameTooltips;
    }
    public OptionInstance<Boolean> tooltipBoxes() {
        return tooltipBoxes;
    }
    public OptionInstance<Boolean> hints() {
        return hints;
    }
    public OptionInstance<Boolean> directSaveLoad() {
        return directSaveLoad;
    }
    public OptionInstance<Difficulty> createWorldDifficulty() {return createWorldDifficulty;}
    public OptionInstance<Boolean> vignette() {
        return vignette;
    }
    public OptionInstance<Boolean> minecartSounds() {
        return minecartSounds;
    }
    public OptionInstance<Boolean> caveSounds() {
        return caveSounds;
    }
    public OptionInstance<Integer> hudScale() {
        return hudScale;
    }
    public OptionInstance<Boolean> vanillaTabs() {
        return vanillaTabs;
    }
    public OptionInstance<Boolean> forceYellowText() {
        return forceYellowText;
    }
    public OptionInstance<Boolean> displayNameTagBorder() {
        return displayNameTagBorder;
    }
    public OptionInstance<Boolean> invertYController() {
        return invertYController;
    }
    public OptionInstance<Integer> controllerIcons() {
        return controllerIcons;
    }
    public OptionInstance<Boolean> invertControllerButtons() {
        return invertControllerButtons;
    }
    public OptionInstance<Integer> selectedController() {
        return selectedController;
    }
    public OptionInstance<Boolean> legacyItemTooltips() {
        return legacyItemTooltips;
    }
}
