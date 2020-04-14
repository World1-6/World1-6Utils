package com.andrew121410.mc.world16utils.sign.screen;

import com.andrew121410.mc.world16utils.chat.Translate;
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

    private ISignPage signScreen;

    private int line = -0;
    private int scroll = 0;
    private int min = 0;
    private int max = 3;

    private SignCache signCache1;

    private boolean hasTextChanged = false;
    private boolean hasScrollChanged = false;

    private boolean isTickerRunning = false;
    private boolean stop = false;

    private List<List<String>> partition = null;

    public SignScreenManager(JavaPlugin plugin, Location location, String name, ISignPage signScreen) {
        this.plugin = plugin;
        this.name = name;
        this.location = location;

        this.signScreen = signScreen;

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
        if (sign != null) this.signScreen.onButton(this, player, line, scroll);
    }

    public void onScroll(Player player, boolean up) {
        if (!this.isTickerRunning) {
            tick(player);
            return;
        }

        if (this.partition == null) {
            return;
        }

        Sign sign = SignUtils.isSign(location.getBlock());
        if (sign != null) {
            if (up) {
                int upONE = this.scroll + 1;
                if (upONE < this.partition.size()) {
                    this.scroll++;
                    for (int i = 0; i <= 3; i++) {
                        String line;
                        if (i < this.partition.get(this.scroll).size()) {
                            line = this.partition.get(this.scroll).get(i);
                        } else line = "";
                        sign.setLine(i, line);
                    }
                    this.signCache1 = new SignCache(sign);
                    this.hasScrollChanged = true;
                }
            } else {
                int downONE = this.scroll - 1;
                if (downONE >= 0 && downONE <= this.partition.size()) {
                    this.scroll--;
                    for (int i = 0; i <= 3; i++) {
                        String line;
                        if (i < this.partition.get(this.scroll).size()) {
                            line = this.partition.get(this.scroll).get(i);
                        } else line = "";
                        sign.setLine(i, line);
                    }
                    this.signCache1 = new SignCache(sign);
                    this.hasScrollChanged = true;
                }
            }
        }
    }

    public void changeLines(Player player) {
        if (!this.isTickerRunning) {
            tick(player);
            return;
        }

        if (this.min <= this.line && this.line < this.max) {
            line++;
        } else {
            line = min;
        }
    }

    public void tick(Player player) {
        if (!this.isTickerRunning) {
            tick();
            player.sendMessage(Translate.chat("&2Sign turned on."));
        }
    }

    private void tick() {
        if (!this.isTickerRunning) {
            this.isTickerRunning = true;
            Sign sign = SignUtils.isSign(location.getBlock());
            if (sign != null) {
                String first = ">";
//                String last = "<";
                new BukkitRunnable() {
                    int temp = 0;
                    String text = sign.getLine(line);
                    SignCache signCache = new SignCache(sign);
                    int oldLine = line;

                    @Override
                    public void run() {
                        if (stop && temp == 0) {
                            isTickerRunning = false;
                            stop = false;
                            this.cancel();
                            return;
                        }

                        if (temp == 0 && !hasTextChanged && !hasScrollChanged) {
                            oldLine = line;
                            signCache.fromSign(sign);
                            text = sign.getLine(line);
                            sign.setLine(line, first + text);
                            if (!sign.update()) stop = true;
                            temp++;
                        } else if (temp > 0 && !hasTextChanged && !hasScrollChanged) {
                            sign.setLine(oldLine, text);
                            if (!sign.update()) stop = true;
                            temp = 0;
                        } else if (hasTextChanged) {
                            signCache1.update(sign);
                            partition = null;
                            oldLine = line;
                            temp = 0;
                            hasTextChanged = false;
                        } else if (hasScrollChanged) {
                            signCache1.update(sign);
                            oldLine = line;
                            temp = 0;
                            hasScrollChanged = false;
                        }
                    }
                }.runTaskTimer(this.plugin, 10L, 10L);
            }
        }
    }

    public void updateSign(Sign sign) {
        tick();
        this.signCache1 = new SignCache(sign);
        this.hasTextChanged = true;
    }

    public void updateSign(Sign sign, List<String> stringList) {
        tick();
        this.partition = Lists.partition(stringList, 4);
        for (int i = 0; i <= 3; i++) {
            String line;
            if (i < this.partition.get(0).size()) {
                line = this.partition.get(0).get(i);
            } else line = "";
            sign.setLine(i, line);
        }
        this.signCache1 = new SignCache(sign);
        this.hasScrollChanged = true;
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

    public ISignPage getSignScreen() {
        return signScreen;
    }

    public void setSignScreen(ISignPage signScreen) {
        this.signScreen = signScreen;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getScroll() {
        return scroll;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
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

    public SignCache getSignCache1() {
        return signCache1;
    }

    public void setSignCache1(SignCache signCache1) {
        this.signCache1 = signCache1;
    }

    public boolean isHasTextChanged() {
        return hasTextChanged;
    }

    public void setHasTextChanged(boolean hasTextChanged) {
        this.hasTextChanged = hasTextChanged;
    }

    public boolean isHasScrollChanged() {
        return hasScrollChanged;
    }

    public void setHasScrollChanged(boolean hasScrollChanged) {
        this.hasScrollChanged = hasScrollChanged;
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

    public List<List<String>> getPartition() {
        return partition;
    }

    public void setPartition(List<List<String>> partition) {
        this.partition = partition;
    }
}