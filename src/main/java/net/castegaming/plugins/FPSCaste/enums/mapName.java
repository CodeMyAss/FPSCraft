package net.castegaming.plugins.FPSCaste.enums;

import java.util.Arrays;
import java.util.Random;

public enum mapName {
	//
	TERMINAL("Terminal"),
	NUKETOWN("Nuketown"),
	VILLA("Villa"),
	FAVELA("Favela"),
	HIGHRISE("Highrise"),
	SUBBASE("Subbase"),
	SKIDROW("Skidrow"),
	FIRINGRANGE("Firing range");
	
	
	private String value;
	
	/*
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;
	*/
	
	/**
	 * x,y,z increased from the base spawn
	 */
	private double[] axis;
	private double[] allies;	
	
	mapName(String value){
		this.value = value;
		handle();
		
	}
	
	public void handle() {
		if (value.equals("Terminal")){
			axis = new double[]{ 35, -20, 29};
			allies = new double[]{ 126, -15, -3};
		} else if (value.equals("Nuketown")){
			axis = new double[]{ 48, -22, -39};
			allies = new double[]{ -53, -22, -38};
		} else if (value.equals("Villa")){
			axis = new double[]{ 35, -20, 29};
			allies = new double[]{ 126, -15, -3};
		} else if (value.equals("Favela")){
			axis = new double[]{ 35, -20, 29};
			allies = new double[]{ 126, -15, -3};
		} else if (value.equals("Highrise")){
			axis = new double[]{ 35, -20, 29};
			allies = new double[]{ 126, -15, -3};
		} else if (value.equals("Subbase")){
			axis = new double[]{ 35, -20, 29};
			allies = new double[]{ 126, -15, -3};
		} else if (value.equals("Skidrow")){
			axis = new double[]{ 35, -20, 29};
			allies = new double[]{ 126, -15, -3};
		} else if (value.equals("Firing range")){
			axis = new double[]{ 35, -20, 29};
			allies = new double[]{ 126, -15, -3};
		} else {
			//no existing map, this could never happen
			axis = null;
			allies = null;
		}
	}
	
	public double[] getAxis(){
		return axis;
	}
	
	public double[] getAllies(){
		return allies;
	}

	public String getName(){
		return value;
	}
	
	@Override
	public String toString(){
		return this.getName();
	}
	
	public static mapName randomMap(){
		int mapNr = new Random().nextInt(mapName.values().length);
		mapName[] maps = mapName.values();
		return maps[mapNr];
	}
	
	public static String getAll(){
		return Arrays.toString(mapName.values()).replace(" ", "");
		
	}
}
