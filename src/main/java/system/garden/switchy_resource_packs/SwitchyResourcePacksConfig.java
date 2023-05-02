package system.garden.switchy_resource_packs;

import org.quiltmc.config.api.WrappedConfig;
import org.quiltmc.config.api.values.ValueList;
import org.quiltmc.config.api.values.ValueMap;

public class SwitchyResourcePacksConfig extends WrappedConfig {
	public boolean enabled = true;
	public final ValueMap<ValueList<String>> presetPacks = ValueMap.builder(ValueList.create("")).build();
}
