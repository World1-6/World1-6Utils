package com.andrew121410.mc.world16utils.sign.screen;

import com.andrew121410.mc.world16utils.chat.LanguageLocale;
import com.andrew121410.mc.world16utils.sign.SignCache;
import com.andrew121410.mc.world16utils.sign.SignUtils;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SignScreenManager {

    private JavaPlugin plugin;

    private String name;
    private Location location;

    private ISignScreen signScreen;

    private boolean needsLineChanged;
    private boolean needsTextChanged;

    private int page = 0;
    private int maxPages = 0;

    private int line = 0;
    private final int maxText = 15;

    private int min = 0;
    private int max = 3;

    private SignCache signCache;

    private List<List<String>> partition = null;

    private boolean isTickerRunning = false;
    private boolean stop = false;

    public static final long DEFAULT_TICK_SPEED = 10L;
    private long tickSpeed;

    public SignScreenManager(JavaPlugin plugin, Location location, String name, Long tickSpeed, ISignScreen signScreen) {
        this.plugin = plugin;
        this.name = name;
        this.location = location;

        this.signScreen = signScreen;

        if (tickSpeed == null) {
            this.tickSpeed = DEFAULT_TICK_SPEED;
        } else this.tickSpeed = tickSpeed;

        Sign sign = SignUtils.isSign(location.getBlock());
        if (sign != null) {
            this.signScreen.onButton(this, null, 0, 0);
        }
    }

    public void onClick(Player player) {
        if (!this.isTickerRunning) {
            tick(player);
            return;
        }

        Sign sign = SignUtils.isSign(location.getBlock());
        if (sign != null) this.signScreen.onButton(this, player, this.line, this.page);
    }

    public void onScroll(Player player, boolean up) {
        if (!this.isTickerRunning) {
            tick(player);
            return;
        }
        if (this.partition == null) return;

        Sign sign = SignUtils.isSign(location.getBlock());
        if (sign == null) return;

        if (this.line <= 0 && up && this.page == 0) {
            player.sendMessage(LanguageLocale.color("&c[this.line <= 0 && up && this.page == 0] WAS RAN")); //DEBUG
            return; //Don't do anything because we are at 0;
        }

        if (up && this.line != 0) {
            this.line--;
            this.needsLineChanged = true;
        } else if (!up && this.line != 3) {
            this.line++;
            this.needsLineChanged = true;
        } else {
            changePage(player, up);
        }
    }

    private void changePage(Player player, boolean up) {
        if (this.partition == null) return;

        if (up) {
            player.sendMessage("ChangePage -> UP WAS RAN.");
            int newPage = this.page - 1;
            if (newPage >= 0 && newPage <= maxPages) {
                player.sendMessage(LanguageLocale.color("&bMax pages reached! [UP - 1]"));
                return;
            }
            this.page = newPage;
        } else {
            player.sendMessage("ChangePage -> DOWN WAS RAN.");
            int newPage = this.page + 1;
            if (newPage < this.maxPages) {
                player.sendMessage(LanguageLocale.color("&bMax pages reached! [DOWN + 1]"));
                return;
            }
            this.page = newPage;
        }

        //@TODO Find a better way to do this.
        this.min = 0;
        this.max = 3;
        this.line = 0;

        this.signCache = fromPartitionToCache(0, 3, this.page);
        this.needsTextChanged = true;
    }

    public void tick(Player player) {
        if (!this.isTickerRunning) {
            tick();
            player.sendMessage(LanguageLocale.color("&2Sign turned on."));
        }
    }

    public void tick() {
        if (!this.isTickerRunning) {
            this.isTickerRunning = true;
            Sign sign = SignUtils.isSign(location.getBlock());
            if (sign == null) throw new NullPointerException("SignScreenManager : tick() : sign == null : NULL");

            new BukkitRunnable() {
                private boolean hold = false;
                int pointer = 0;
                int oldLine = line;
                private final SignCache signCacheBuffer = new SignCache(sign); //Used to save the sign before we make changes to it.
                private StringBuilder stringBuilder = new StringBuilder(); //Used to add the pointer.

                @Override
                public void run() {
                    if (stop && pointer == 0) {
                        isTickerRunning = false;
                        stop = false;
                        this.cancel();
                        return;
                    }

                    //Holding
                    if (hold) return;

                    if (pointer == 0 && !needsLineChanged && !needsTextChanged) {
                        this.stringBuilder = new StringBuilder(); //Clear the StringBuffer.
                        this.stringBuilder.append("*").append(sign.getLine(oldLine)); //Add the line.

                        this.stringBuilder.setCharAt(0, '>'); //Add pointer.
                        sign.setLine(this.oldLine, this.stringBuilder.toString()); //Set the line on the sign.
                        if (!sign.update()) stop = true; //Update the sign.
                        pointer++;
                    } else if (pointer == 1 && !needsLineChanged && !needsTextChanged) {
                        if (!this.signCacheBuffer.update(sign)) stop = true; //Reset the sign back to normal.
                        pointer = 0;
                    } else if (needsTextChanged) {
                        oldLine = line;
                        signCache.update(sign); //Update the sign.
                        this.signCacheBuffer.fromSign(sign); //Update the sign cache before we makes changes to the sign.
                        pointer = 0;
                        needsTextChanged = false;
                    } else if (needsLineChanged) {
                        oldLine = line; //Get the new line number.
                        pointer = 0;
                        needsLineChanged = false;
                    }
                }
            }.runTaskTimer(this.plugin, this.tickSpeed, this.tickSpeed);
        }
    }

    public void updateSign(Sign sign, List<String> stringList) {
        this.partition = Lists.partition(stringList, 4);
        this.maxPages = this.partition.size();
        this.signCache = fromPartitionToCache(0, 3, 0);
        this.needsTextChanged = true;
    }

    private SignCache fromPartitionToCache(int min, int max, int page) {
        SignCache signCacheTemp = new SignCache();
        for (int i = min; i <= max; i++) {
            String line;
            //If sign does have data on the line grab it if not then set it to "";
            if (i < this.partition.get(page).size()) {
                line = this.partition.get(page).get(i);
            } else line = "";
            signCacheTemp.setLine(i, line);
        }
        return signCacheTemp;
    }

    public Sign getSign() {
        return SignUtils.isSign(location.getBlock());
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ISignScreen getSignScreen() {
        return signScreen;
    }

    public void setSignScreen(ISignScreen signScreen) {
        this.signScreen = signScreen;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = maxPages;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getMaxText() {
        return maxText;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public SignCache getSignCache() {
        return signCache;
    }

    public void setSignCache(SignCache signCache) {
        this.signCache = signCache;
    }

    public List<List<String>> getPartition() {
        return partition;
    }

    public void setPartition(List<List<String>> partition) {
        this.partition = partition;
    }

    public boolean isTickerRunning() {
        return isTickerRunning;
    }

    public void setTickerRunning(boolean tickerRunning) {
        isTickerRunning = tickerRunning;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public long getTickSpeed() {
        return tickSpeed;
    }

    public void setTickSpeed(long tickSpeed) {
        this.tickSpeed = tickSpeed;
    }
}