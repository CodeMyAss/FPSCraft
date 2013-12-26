package net.castegaming.plugins.FPSCaste.commands;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import net.castegaming.plugins.FPSCaste.FPSCaste;
import net.castegaming.plugins.FPSCaste.FPSPlayer;
import net.castegaming.plugins.FPSCaste.config.Config;
import net.castegaming.plugins.FPSCaste.exceptions.NoOnlinePlayerException;
import net.castegaming.plugins.FPSCaste.exceptions.NotAdminException;
import net.castegaming.plugins.FPSCaste.exceptions.NotIngameException;
import net.castegaming.plugins.FPSCaste.map.MapPreset;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class EditMapCommand extends FPSAdminCommand {

	public EditMapCommand(CommandSender sender, String[] args) throws NotAdminException, NotIngameException, NoOnlinePlayerException {
		super(sender, args, true);
	}

	
	@Override
	public boolean handle() {
		FPSPlayer player = FPSCaste.getFPSPlayer(getName());
		if (player.getSelectedMap() != null){
			MapPreset preset = player.getSelectedMap();
			YamlConfiguration config = Config.getMap(preset.getName());
			if (args.length > 0 && args[0].length() > 0){
				Location l = player.getPlayer().getLocation();
				Float[] main = preset.getSpawn();
				Float[] point = new Float[]{((float) l.getX())-main[0], ((float) l.getY())-main[1], ((float) l.getZ())-main[2], l.getYaw(), l.getPitch()};
				String location = point[0] + " " + point[1] + " " + point[2] + " " + point[3] + " " + point[4];
				
				if (MapPreset.presets.containsKey(args[0])){
					player.setSelectedMap(MapPreset.presets.get(args[0]));
					goodMsg("Changed currently selected preset to: " + args[0]);
				} else if (args[0].equalsIgnoreCase("domA")){
					preset.setDomA(point);
					config.set("domA", location);
					save(config);
					goodMsg("Set the dominaion flag A to your position!");
				} else if (args[0].equalsIgnoreCase("domB")){
					preset.setDomB(point);
					config.set("domB", location);
					save(config);
					goodMsg("Set the dominaion flag B to your position!");
				} else if (args[0].equalsIgnoreCase("domC")){
					preset.setDomC(point);
					config.set("domC", location);
					save(config);
					goodMsg("Set the dominaion flag C to your position!");
				} else if (args[0].equalsIgnoreCase("BombA")){
					preset.setBombA(point);
					config.set("bombA", location);
					save(config);
					goodMsg("Set the bomb A to your position!");
				} else if (args[0].equalsIgnoreCase("BombB")){
					preset.setBombB(point);
					config.set("bombB", location);
					save(config);
					goodMsg("Set the bomb B to your position!");
				} else if (args[0].equalsIgnoreCase("allies")){
					preset.setSpawnAllies(point);
					config.set("allies", location);
					save(config);
					goodMsg("Set Allies spawn to your position!");
				} else if (args[0].equalsIgnoreCase("axis")){
					preset.setSpawnAxis(point);
					config.set("axis", location);
					save(config);
					goodMsg("Set Axis spawn to your position!");
				} else if (args[0].equalsIgnoreCase("spawn")){
					boolean b = preset.addSpawnpoint(point);
					if (b){
						List<String> spawns = new LinkedList<String>();
						
						for (Float[] loc : preset.getSpawnpoints()){
							spawns.add(Arrays.toString(loc).replace("[", "").replace("]", "").replaceAll(",", ""));
						}
						
						config.set("spawnpoints", spawns);
						save(config);
						goodMsg("Created a new spawn at your position!");
					} else {
						badMsg("There was allready a spawn at this location!");
					}
				} else if (args[0].equalsIgnoreCase("list")){
					goodMsg("Available presets: ");
					boolean edited = false;
					for (String s : MapPreset.presets.keySet()){
						goodMsg(s);
						edited = true;
					}
					
					if (!edited) badMsg("NONE!");
				} else if (args[0].equalsIgnoreCase("main")){
					preset.setSpawn(new Float[]{(float) l.getX(), (float) l.getY(), (float) l.getZ(), l.getYaw(), l.getPitch()});
					config.set("main", l.getX() + " " + l.getY() + " " + l.getZ() + " " + l.getYaw() + " " + l.getPitch());
					save(config);
					goodMsg("Set main spawn to your position!");
				} else if (args[0].startsWith("desc")){
					if (args.length > 1 && args[1].length() > 0){
						String desc = Arrays.toString(Arrays.copyOfRange(args, 1, args.length)).replace("[", "").replace("]", "");
						preset.setDescription(desc);
						config.set("description", desc);
						save(config);
						goodMsg("Set the description to: " + desc);
					} else {
						goodMsg("Current description is:");
						goodMsg(preset.getDescription());
					}
				} else if (args[0].equalsIgnoreCase("name")){
					if (args.length > 1 && args[1].length() > 0){
						File old = new File(FPSCaste.getInstance().getDataFolder() + File.separator + "maps", preset.getName());
						Config.saveMapConfig(args[1], Config.getMap(preset.getName()));
						try {
							old.delete();
						} catch (Exception e){
							FPSCaste.log("Could not delete the map file: " + preset.getName(), Level.WARNING);
						}
						 
						goodMsg("Changed the name from " + preset.getName() + " to: " + args[1]);
						preset.setName(args[1]);
					} else {
						goodMsg("The current name of your selected preset is: " + preset.getName() + ". To change this, typ /editmap name [name]!");
					}
				} else if (args[0].equalsIgnoreCase("select")){
					if (args.length > 1 && args[1].length() > 0){
						if (MapPreset.presets.containsKey(args[1])){
							player.setSelectedMap(MapPreset.presets.get(args[1]));
							goodMsg("Changed currently selected preset to: " + args[1]);
						} else {
							goodMsg("Possible presets: " + MapPreset.presets.keySet().toString().replace("[", "").replace("]", ""));
						}
					} else {
						goodMsg("Possible presets: " + MapPreset.presets.keySet().toString().replace("[", "").replace("]", ""));
					}
				} else {
					badMsg("Could not find the specific command! Options are: doma, domb, domc, bomba, bombb, spawn, name, desc, main, allies and axis");
				}
			} else {
				goodMsg(preset.check());
			}
		} else {
			if (args.length > 0 && MapPreset.presets.containsKey(args[0])){
				player.setSelectedMap(MapPreset.presets.get(args[0]));
				goodMsg("Changed currently selected preset to: " + args[0]);
			} else {
				goodMsg("Possible presets1: " + MapPreset.presets.keySet().toString().replace("[", "").replace("]", ""));
			}
		}
		return true;
	}
	
	public void save(YamlConfiguration conf){
		Config.saveMapConfig((FPSCaste.getFPSPlayer(getName()).getSelectedMap().getName()), conf);
	}
	
	public double[] parse(String s){
		String[] intS = s.split(" ");
		double[] coordinates = new double[3];
		
		try {
			for (int i=0; i<intS.length; i++){
				coordinates[i] = Double.parseDouble(intS[i]);
			}
			return coordinates;
		} catch (Exception e) {
			return null;
		}
	}
}
