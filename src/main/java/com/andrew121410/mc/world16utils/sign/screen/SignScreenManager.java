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
            tick();
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

        if (this.line <= 0 && up) {
            player.sendMessage(LanguageLocale.color("&cthis.line <= 0 && up WAS RAN")); //DEBUG
            return; //Don't do anything because we are at 0;
        }

        if (this.line < this.max && !up) {
            this.line++;
            this.needsLineChanged = true;
        } else if (this.line != this.min && up) {
            this.line--;
            this.needsLineChanged = true;
        } else {
            changePage(player, up);
        }
    }

    private void changePage(Player player, boolean up) {
        if (!this.isTickerRunning) {
            tick(player);
            return;
        }
        if (this.partition == null) return;

        SignCache newSignCache = new SignCache(getSign());

        //@TODO Find a better way to do this.
        this.min = 0;
        this.max = 3;
        this.line = 0;

        if (up) {
            player.sendMessage("&6changePage -> UP WAS RAN.");
            int newPage = this.page - 1;
            if (newPage >= this.maxPages) {
                player.sendMessage(LanguageLocale.color("&bMax pages reached! [UP - 1]"));
                return;
            }
            for (int i = 0; i <= 3; i++) {
                String line;
                if (i < this.partition.get(newPage).size()) {
                    line = this.partition.get(newPage).get(i);
                } else line = "";
                newSignCache.setLine(i, line);
            }
            this.page = newPage;
        } else {
            player.sendMessage("&6changePage -> DOWN WAS RAN.");
            int newPage = this.page + 1;
            if (newPage >= this.maxPages) {
                player.sendMessage(LanguageLocale.color("&bMax pages reached! [DOWN + 1]"));
                return;
            }
            for (int i = 0; i <= 3; i++) {
                String line;
                if (i < this.partition.get(newPage).size()) {
                    line = this.partition.get(newPage).get(i);
                } else line = "";
                newSignCache.setLine(i, line);
            }
            this.page = newPage;
        }
        this.signCache = newSignCache;
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
                int pointer = 0;
                private boolean hold = false;
                private final SignCache signCacheBuffer = new SignCache(sign);
                int oldLine = line;
                private StringBuffer stringBuffer = new StringBuffer();

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
                        this.stringBuffer.setCharAt(0, '>');
                        sign.setLine(this.oldLine, this.stringBuffer.toString());
                        if (!sign.update()) stop = true;
                        pointer++;
                    } else if (pointer == 1 && !needsLineChanged && !needsTextChanged) {
                        this.stringBuffer.setCharAt(0, ' ');
                        sign.setLine(this.oldLine, this.stringBuffer.toString());
                        if (!sign.update()) stop = true;
                        pointer = 0;
                    } else if (needsTextChanged) {
                        oldLine = line;
                        signCache.update(sign);
                        this.signCacheBuffer.fromSign(sign);
                        this.stringBuffer = new StringBuffer();
                        stringBuffer.append(" ").append(sign.getLine(0));
                        pointer = 0;
                        needsTextChanged = false;
                    } else if (needsLineChanged) {
                        oldLine = line;
                        this.stringBuffer = new StringBuffer();
                        stringBuffer.append(" ").append(sign.getLine(oldLine));
                        pointer = 0;
                        needsLineChanged = false;
                    }
                }
            }.runTaskTimer(this.plugin, this.tickSpeed, this.tickSpeed);
        }
    }

    public void updateSign(Sign sign, List<String> stringList) {
        tick();
        this.partition = Lists.partition(stringList, 4);
        this.maxPages = this.partition.size();
        for (int i = 0; i <= 3; i++) {
            String line;
            //If sign does have data on the line grab it if not then set it to "";
            if (i < this.partition.get(0).size()) {
                line = this.partition.get(0).get(i);
            } else line = "";
            sign.setLine(i, line);
        }
        this.signCache = new SignCache(sign);
        this.needsTextChanged = true;
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