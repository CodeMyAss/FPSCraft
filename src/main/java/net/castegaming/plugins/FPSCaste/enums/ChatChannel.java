package net.castegaming.plugins.FPSCaste.enums;

public enum ChatChannel {
	ALL(),
	MATCH(),
	TEAM();
	
	public ChatChannel next(){
		ChatChannel channels[] = ChatChannel.values();  
         int ordinal = this.ordinal();  
         ordinal = ++ordinal % channels.length;  
         return channels[ordinal]; 
	}
}
