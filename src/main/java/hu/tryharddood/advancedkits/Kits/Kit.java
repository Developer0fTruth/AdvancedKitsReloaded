package hu.tryharddood.advancedkits.Kits;

import hu.tryharddood.advancedkits.AdvancedKits;
import hu.tryharddood.advancedkits.Variables;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Kit {
	private final Object[] flags = new Object[Flags.values().length];

	private final YamlConfiguration saveFile;
	private       String            kitname;

	private ArrayList<ItemStack> itemStacks = new ArrayList<>();
	private ArrayList<ItemStack> armor      = new ArrayList<>();

	private boolean save = false;

	private ArrayList<String> worlds   = new ArrayList<>();
	private ArrayList<String> commands = new ArrayList<>();

	public Kit(String kitname) {
		this.kitname = kitname;
		saveFile = YamlConfiguration.loadConfiguration(getSaveFile());
	}

	public String getName() {
		return this.kitname;
	}

	public ArrayList<ItemStack> getItemStacks() {
		return this.itemStacks;
	}

	public boolean AddItem(ItemStack a) {
		itemStacks.add(a);

		List<Map<String, Object>> list = itemStacks.stream().map(ItemStack::serialize).collect(Collectors.toList());
		setProperty("Items", list);
		return true;
	}

	public void createKit(List<ItemStack> itemStacks, List<ItemStack> armors) {
		setFlag(Flags.VISIBLE, true);
		setFlag(Flags.PERMISSION, Variables.KIT_USE_KIT_PERMISSION.replace("[kitname]", getName()));
		setFlag(Flags.CLEARINV, false);
		setFlag(Flags.FIRSTJOIN, false);
		setFlag(Flags.REPLACEARMOR, false);
		setFlag(Flags.COST, 0);
		setFlag(Flags.DELAY, 0.0);
		setFlag(Flags.ICON, Material.EMERALD_BLOCK.toString());

		itemStacks.forEach(this::AddItem);
		armors.forEach(this::AddArmor);

		AdvancedKits.getKitManager().load();
	}

	public ArrayList<ItemStack> getArmor() {
		return this.armor;
	}

	public void setArmor(ArrayList<ItemStack> armor) {
		this.armor = armor;

		List<Map<String, Object>> list = this.armor.stream().map(ItemStack::serialize).collect(Collectors.toList());
		setProperty("Armor", list);
	}

	public boolean AddArmor(ItemStack a) {
		this.armor.add(a);

		List<Map<String, Object>> list = this.armor.stream().map(ItemStack::serialize).collect(Collectors.toList());
		setProperty("Armor", list);
		return true;
	}

	public File getSaveFile() {
		File file = new File(AdvancedKits.getInstance().getDataFolder() + File.separator + "kits" + File.separator + kitname + ".yml");
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				AdvancedKits.log(ChatColor.RED + "Please send this to the author of this plugin:");
				AdvancedKits.log(" -- StackTrace --");
				e.printStackTrace();
				System.out.println(" -- End of StackTrace --");
			}
		}
		return file;
	}

	public YamlConfiguration getYaml() {
		return this.saveFile;
	}

	private void setProperty(String key, Object value) {
		try
		{
			saveFile.load(getSaveFile());
			saveFile.set(key, value);
			saveFile.save(getSaveFile());
		} catch (Exception e)
		{
			AdvancedKits.log("Couldn't set property '" + key + "' for '" + this.kitname + "'");
			AdvancedKits.log(ChatColor.RED + "Please send this to the author of this plugin:");
			AdvancedKits.log(" -- StackTrace --");
			e.printStackTrace();
			System.out.println(" -- End of StackTrace --");
		}
	}

	public ArrayList<String> getWorlds() {
		return this.worlds;
	}

	public void AddWorld(String w1) {
		if (!this.worlds.contains(w1))
		{
			this.worlds.add(w1);
		}

		setProperty("Flags.Worlds", this.worlds);
	}

	public void RemoveWorld(String w1) {
		if (this.worlds.contains(w1))
		{
			this.worlds.remove(w1);
		}

		setProperty("Flags.Worlds", this.worlds);
	}

	public ArrayList<String> getCommands() {
		return this.commands;
	}

	public void AddCommand(String w1) {
		if (!this.commands.contains(w1))
		{
			this.commands.add(w1);
		}

		setProperty("Flags.Commands", this.commands);
	}

	public void RemoveCommand(String w1) {
		if (this.commands.contains(w1))
		{
			this.commands.remove(w1);
		}

		setProperty("Flags.Commands", commands);
	}

	public boolean isVisible() {
		return (boolean) getFlag(Flags.VISIBLE, true);
	}

	public boolean isClearinv() {
		return (boolean) getFlag(Flags.CLEARINV, false);
	}

	public boolean getDefaultUnlock() {
		return (boolean) getFlag(Flags.UNLOCKED, false);
	}

	public String getPermission() {
		return (String) getFlag(Flags.PERMISSION, Variables.KIT_USE_KIT_PERMISSION.replace("[kitname]", getName()));
	}

	public String getDisplayName() {

		return (String) getFlag(Flags.DISPLAYNAME, getName());
	}

	public Material getIcon() {
		return Material.matchMaterial(getFlag(Flags.ICON, Material.EMERALD_BLOCK.toString()).toString());
	}

	public int getCost() {
		return (int) getFlag(Flags.COST, 0);
	}

	public int getUses() {
		return (int) getFlag(Flags.USES, 0);
	}

	public double getDelay() {
		return (double) getFlag(Flags.DELAY, 0.0);
	}

	public void setDelay(double delay) {
		setFlag(Flags.DELAY, delay);
	}

	public boolean isFirstjoin() {
		return (boolean) getFlag(Flags.FIRSTJOIN, false);
	}

	public boolean getReplaceArmor() {return (boolean) getFlag(Flags.REPLACEARMOR, false);}

	public void setFlag(Flags flag, Object value) {
		this.flags[flag.getId()] = value;
		setProperty("Flags." + flag.toString(), value);
	}

	public Object getFlag(Flags flag, Object defvalue) {
		if (this.flags[flag.getId()] == null)
		{
			return defvalue;
		}
		return this.flags[flag.getId()];
	}

	public Object getFlag(Flags flag) {
		return getFlag(flag, null);
	}
}
