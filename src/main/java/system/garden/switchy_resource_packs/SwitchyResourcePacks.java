package system.garden.switchy_resource_packs;

import net.minecraft.client.MinecraftClient;
import org.quiltmc.config.api.values.ValueList;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.config.QuiltConfig;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.client.ClientResourceLoaderEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchyResourcePacks implements ClientModInitializer {
	public static final String ID = "switchy_resource_packs";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final SwitchyResourcePacksConfig CONFIG = QuiltConfig.create(ID, "packs", SwitchyResourcePacksConfig.class);

	@Override
	public void onInitializeClient(ModContainer mod) {
		CONFIG.presetPacks.put("default", ValueList.create("bean"));
		ClientResourceLoaderEvents.END_RESOURCE_PACK_RELOAD.register((client, resourceManager, first, error)->{
			CONFIG.presetPacks.get("default").addAll(MinecraftClient.getInstance().getResourcePackManager().getEnabledNames());
		});
	}
}
