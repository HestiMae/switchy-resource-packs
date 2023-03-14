package system.garden.switchy_resource_packs;

import folk.sisby.switchy.api.events.SwitchySwitchEvent;
import folk.sisby.switchy.client.api.SwitchyClientEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import net.minecraft.resource.pack.ResourcePackManager;
import org.quiltmc.loader.api.config.QuiltConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchyResourcePacks implements SwitchyClientEvents.Switch {
	public static final String ID = "switchy_resource_packs";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final SwitchyResourcePacksConfig CONFIG = QuiltConfig.create(ID, "config", SwitchyResourcePacksConfig.class);

	@Override
	public void onSwitch(SwitchySwitchEvent event) {
		Session session = MinecraftClient.getInstance().getSession();
		if (CONFIG.enabled && session.getPlayerUuid() != null && event.player().equals(session.getPlayerUuid()))
		{
			ResourcePackManager resourcePackManager = MinecraftClient.getInstance().getResourcePackManager();
			if (event.previousPreset() != null)
			{
				LOGGER.info("[Switchy Resource Packs] Saving resource packs for preset {}", event.previousPreset());
				CONFIG.presetPacks.putIfAbsent(event.previousPreset(), CONFIG.presetPacks.getDefaultValue());
				CONFIG.presetPacks.get(event.previousPreset()).clear();
				CONFIG.presetPacks.get(event.previousPreset()).addAll(resourcePackManager.getEnabledNames());

			}
			if (event.currentPreset() != null && CONFIG.presetPacks.containsKey(event.currentPreset()) && !resourcePackManager.getEnabledNames().equals(CONFIG.presetPacks.get(event.currentPreset())))
			{
				LOGGER.info("[Switchy Resource Packs] Switching to resource packs for preset {}", event.currentPreset());
				resourcePackManager.setEnabledProfiles(CONFIG.presetPacks.get(event.currentPreset()));
				MinecraftClient.getInstance().reloadResources();
			}
		}
	}
}
