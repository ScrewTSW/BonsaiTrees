package com.davenonymous.bonsaitrees2.setup;

import com.davenonymous.bonsaitrees2.block.ModObjects;
import com.davenonymous.bonsaitrees2.command.ModCommands;
import com.davenonymous.bonsaitrees2.compat.jei.BonsaiTrees2JEIPlugin;
import com.davenonymous.bonsaitrees2.registry.SoilCompatibility;
import com.davenonymous.bonsaitrees2.render.TreeModels;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class ForgeEventHandlers {
    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
        ModCommands.register(event.getServer().getCommandManager().getDispatcher());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void startServer(FMLServerAboutToStartEvent event) {
        IReloadableResourceManager manager = (IReloadableResourceManager) Minecraft.getInstance().getResourceManager();
        manager.addReloadListener((IResourceManagerReloadListener) resourceManager -> {
            SoilCompatibility.INSTANCE.update(event.getServer().getRecipeManager().getRecipes());
        });
    }

    @SubscribeEvent
    public void recipesUpdated(RecipesUpdatedEvent event) {
        if(ModList.get().isLoaded("jei")) {
            BonsaiTrees2JEIPlugin.saplings = ModObjects.saplingRecipeHelper.getRecipesList(event.getRecipeManager());
        }

        SoilCompatibility.INSTANCE.update(event.getRecipeManager().getRecipes());
        TreeModels.init();
    }
}
