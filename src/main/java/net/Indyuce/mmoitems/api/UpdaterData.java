package net.Indyuce.mmoitems.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import net.Indyuce.mmoitems.manager.UpdaterManager.KeepOption;
import net.mmogroup.mmolib.api.item.NBTItem;

public class UpdaterData {

	private final Type type;
	private final String id;

	/*
	 * two UUIDs can be found : one on the itemStack in the nbttags, and one in
	 * the UpdaterData instance. if the two match, the item is up to date. if
	 * they don't match, the item needs to be updated
	 */
	private final UUID uuid;

	private final Set<KeepOption> options = new HashSet<>();

	public UpdaterData(Type type, String id, ConfigurationSection config) {
		this(type, id, UUID.fromString(config.getString("uuid")));

		for (KeepOption option : KeepOption.values())
			if (config.getBoolean(option.getPath()))
				options.add(option);
	}

	public UpdaterData(Type type, String id, UUID uuid, KeepOption... options) {
		this.uuid = uuid;
		this.type = type;
		this.id = id;
		this.options.addAll(Arrays.asList(options));
	}

	public UpdaterData(Type type, String id, UUID uuid, boolean enableAllOptions) {
		this(type, id, uuid);

		if (enableAllOptions)
			options.addAll(Arrays.asList(KeepOption.values()));
	}

	public void save(ConfigurationSection config) {
		for (KeepOption option : KeepOption.values())
			if (options.contains(option))
				config.set(option.getPath(), true);
		config.set("uuid", uuid.toString());
	}

	public String getPath() {
		return type.getId() + "." + id;
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public boolean matches(NBTItem item) {
		return uuid.toString().equals(item.getString("MMOITEMS_ITEM_UUID"));
	}

	public boolean hasOption(KeepOption option) {
		return options.contains(option);
	}

	public void addOption(KeepOption option) {
		options.add(option);
	}

	public void removeOption(KeepOption option) {
		options.remove(option);
	}
}