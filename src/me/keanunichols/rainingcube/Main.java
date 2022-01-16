package me.keanunichols.rainingcube;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;


public class Main extends JavaPlugin {
	
	private Random random;
	private Main instance;
	
	private Random getRandom() {
        return this.random;
    }
	
	public int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	public boolean getRandomBoolean() {
	    Random random = new Random();
	    return random.nextBoolean();
	}
	
	public void onEnable() {
		this.random = new Random();
		this.instance = this;
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            private Random rand;

			@Override
            public void run() {
            	
            	if(Bukkit.getOnlinePlayers().size() == 0)
            		return;
            	
            	ArrayList<Player> players = new ArrayList<Player>();
            	for(Player p: Bukkit.getOnlinePlayers()) {
            		players.add(p);
            	}
            	
            	Collections.shuffle(players);
            	  
            	int randomSize = getRandom().nextInt(players.size());
            	final Set<Entity> entitySet = new HashSet<Entity>();
            	for(int i=0; i<=randomSize; i++) {
            		Player plr = players.get(i);
            		int numMobs = getRandomNumber(0,3);
            		
            		for(int j=0; j<numMobs; j++) {
            			this.rand = new Random();
            			Location pLocation = plr.getLocation();
            			Chunk pChunk = pLocation.getChunk();
            			int plrY = pLocation.getBlockY();
            			Location randLoc = pChunk.getBlock(this.rand.nextInt(16), plrY + getRandomNumber(5,10),this.rand.nextInt(16)).getLocation();
            			if(getRandomBoolean()) {
            				MagmaCube mCube = (MagmaCube)plr.getWorld().spawnEntity(randLoc, EntityType.MAGMA_CUBE);
            				int csize = getRandomNumber(5,10);
            				mCube.setSize(csize);
            				mCube.setTarget(plr);
            				entitySet.add(mCube);
            			}else {
            				Slime sCube = (Slime)plr.getWorld().spawnEntity(randLoc, EntityType.SLIME);
            				int csize = getRandomNumber(5,10);
            				sCube.setSize(csize);
            				sCube.setTarget(plr);
            				entitySet.add(sCube);
            			}
            			
            		}
            	}
            	
            	
            	new BukkitRunnable() {
                    public void run()
                    {	
                        for (final Entity entity : entitySet) {
                            entity.remove();
                        }
                    }
                }.runTaskLater((instance), 2400L);
			}
        }, 1200L, 1200L);
    }

}
