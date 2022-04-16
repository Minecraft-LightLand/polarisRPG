package org.xkmc.polaris_rpg.compat;

import dev.lcy0x1.base.Proxy;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.util.ResourceLocation;
import org.xkmc.polaris_rpg.compat.recipes.MagicCraftRecipeCategory;
import org.xkmc.polaris_rpg.init.PolarisRPG;
import org.xkmc.polaris_rpg.init.registry.PolarisBlocks;
import org.xkmc.polaris_rpg.init.registry.PolarisRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PolarisJeiPlugin implements IModPlugin {

	public static PolarisJeiPlugin INSTANCE;

	public final ResourceLocation UID = new ResourceLocation(PolarisRPG.MODID, "jei_plugin");

	public final MagicCraftRecipeCategory MAGIC_CRAFT = new MagicCraftRecipeCategory();


	public PolarisJeiPlugin() {
		INSTANCE = this;
	}

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration) {

	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(MAGIC_CRAFT.init(helper));
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(Proxy.getWorld().getRecipeManager().getAllRecipesFor(PolarisRecipeTypes.RT_CRAFT), MAGIC_CRAFT.getUid());
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(PolarisBlocks.RITUAL_CORE.get().asItem().getDefaultInstance(), MAGIC_CRAFT.getUid());
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

}
