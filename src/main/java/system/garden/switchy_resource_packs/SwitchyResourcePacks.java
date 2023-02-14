package system.garden.switchy_resource_packs;

import folk.sisby.switchy.client.api.SwitchyEventsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import net.minecraft.resource.pack.ResourcePackManager;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.config.QuiltConfig;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SwitchyResourcePacks implements ClientModInitializer {
	public static final String ID = "switchy_resource_packs";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final SwitchyResourcePacksConfig CONFIG = QuiltConfig.create(ID, "config", SwitchyResourcePacksConfig.class);

	@Override
	public void onInitializeClient(ModContainer mod) {
		SwitchyEventsClient.registerSwitchListener(new Identifier(ID, "reload_listener"), (switchEvent)-> {
			Session session = MinecraftClient.getInstance().getSession();
			if (CONFIG.enabled && session.getPlayerUuid() != null && switchEvent.player.equals(session.getPlayerUuid()))
			{
				List<String> resourcePacks = MinecraftClient.getInstance().options.resourcePacks;
				ResourcePackManager resourcePackManager = MinecraftClient.getInstance().getResourcePackManager();
				if (switchEvent.previousPreset != null)
				{
					CONFIG.presetPacks.putIfAbsent(switchEvent.previousPreset, CONFIG.presetPacks.getDefaultValue());
					CONFIG.presetPacks.get(switchEvent.previousPreset).clear();
					CONFIG.presetPacks.get(switchEvent.previousPreset).addAll(resourcePacks);

				}
				if (CONFIG.presetPacks.containsKey(switchEvent.currentPreset) && !resourcePacks.equals(CONFIG.presetPacks.get(switchEvent.currentPreset)))
				{
					LOGGER.info("[Switchy Resource Packs] Switching to resource packs for preset {}", switchEvent.currentPreset);
					resourcePackManager.setEnabledProfiles(CONFIG.presetPacks.get(switchEvent.currentPreset));
					MinecraftClient.getInstance().reloadResources();
				}
			}
		});
	}
}
