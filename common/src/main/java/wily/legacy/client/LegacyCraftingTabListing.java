package wily.legacy.client;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import wily.legacy.Legacy4J;
import wily.legacy.util.CompoundTagUtil;
import wily.legacy.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import static wily.legacy.util.CompoundTagUtil.getJsonStringOrNull;
import static wily.legacy.util.CompoundTagUtil.ifJsonStringNotNull;

public class LegacyCraftingTabListing {
    public static final List<LegacyCraftingTabListing> list = new ArrayList<>();
    private static final String CRAFTING_TAB_LISTING = "crafting_tab_listing.json";
    public String id;
    public Component displayName;
    public ResourceLocation icon;
    public CompoundTag itemIconTag;
    public final Map<String,List<RecipeValue<CraftingContainer, CraftingRecipe>>> craftings = new LinkedHashMap<>();


    public LegacyCraftingTabListing(String id, Component displayName, ResourceLocation icon, CompoundTag itemIconTag){
        this.id = id;
        this.displayName = displayName;
        this.icon = icon;
        this.itemIconTag = itemIconTag;
    }
    public boolean isValid(){
        return displayName != null && id != null && !craftings.isEmpty() && icon != null;
    }
    public static class Manager extends SimplePreparableReloadListener<List<LegacyCraftingTabListing>> {
        @Override
        protected List<LegacyCraftingTabListing> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
            List<LegacyCraftingTabListing> listings = new ArrayList<>();
            ResourceManager manager = Minecraft.getInstance().getResourceManager();
            manager.getNamespaces().stream().sorted(Comparator.comparingInt(s-> s.equals("legacy") ? 0 : 1)).forEach(name->manager.getResource(new ResourceLocation(name, CRAFTING_TAB_LISTING)).ifPresent(r->{
                try {
                    BufferedReader bufferedReader = r.openAsReader();
                    JsonObject obj = GsonHelper.parse(bufferedReader);
                    obj.asMap().forEach((c,ce)->{
                        if (ce instanceof JsonObject go) {
                            JsonElement listingElement = go.get("listing");
                            listings.stream().filter(l-> c.equals(l.id)).findFirst().ifPresentOrElse(l->{
                                ifJsonStringNotNull(go,"displayName", Component::translatable, n-> l.displayName = n);
                                ifJsonStringNotNull(go,"icon", ResourceLocation::new, n-> l.icon = n);
                                ifJsonStringNotNull(go,"nbt", CompoundTagUtil::parseCompoundTag, n-> l.itemIconTag = n);
                                JsonUtil.addGroupedRecipeValuesFromJson(l.craftings,listingElement);
                            }, ()->{
                                LegacyCraftingTabListing listing = new LegacyCraftingTabListing(c,getJsonStringOrNull(go,"displayName",Component::translatable),getJsonStringOrNull(go,"icon",ResourceLocation::new), getJsonStringOrNull(go,"nbt",CompoundTagUtil::parseCompoundTag));
                                JsonUtil.addGroupedRecipeValuesFromJson(listing.craftings,listingElement);
                                listings.add(listing);
                            });
                        }
                    });
                    bufferedReader.close();
                } catch (IOException exception) {
                    Legacy4J.LOGGER.warn(exception.getMessage());
                }
            }));
            return listings;
        }


        @Override
        protected void apply(List<LegacyCraftingTabListing> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
            list.clear();
            list.addAll(object);;
        }
    }
}
