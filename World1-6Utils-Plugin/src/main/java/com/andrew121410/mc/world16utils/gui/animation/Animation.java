package com.andrew121410.mc.world16utils.gui.animation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Animation {

    private static final long FRAME_DURATION_MS = 50;
    private static final int FADING_FRAMES = 40;

    private static final Map<String, List<Component>> CACHE = new ConcurrentHashMap<>();

    /**
     * Color sweeps across each character of the text.
     * Colors define the gradient — at least 2 recommended.
     */
    public static Component wave(String text, TextColor... colors) {
        String key = "wave|" + text + "|" + colorKey(colors);
        List<Component> frames = CACHE.computeIfAbsent(key, k -> buildWaveFrames(text, colors));
        return frames.get(currentIndex(frames.size()));
    }

    /**
     * Entire text fades between the provided colors.
     * Colors define the gradient — at least 2 recommended.
     */
    public static Component fading(String text, TextColor... colors) {
        String key = "fading|" + text + "|" + colorKey(colors);
        List<Component> frames = CACHE.computeIfAbsent(key, k -> buildFadingFrames(text, colors));
        return frames.get(currentIndex(frames.size()));
    }

    private static int currentIndex(int frameCount) {
        return (int) ((System.currentTimeMillis() / FRAME_DURATION_MS) % frameCount);
    }

    // --- Frame builders ---

    private static List<Component> buildWaveFrames(String text, TextColor[] colors) {
        char[] chars = text.toCharArray();
        int gradientSize = Math.max(chars.length * 2, 2);
        List<TextColor> gradient = buildGradient(colors, gradientSize);

        List<Component> frames = new ArrayList<>(gradientSize);
        for (int frame = 0; frame < gradientSize; frame++) {
            Component line = Component.empty();
            for (int j = 0; j < chars.length; j++) {
                TextColor color = gradient.get((frame + j) % gradientSize);
                line = line.append(Component.text(chars[j]).color(color)
                        .decoration(TextDecoration.ITALIC, false));
            }
            frames.add(line);
        }
        return frames;
    }

    private static List<Component> buildFadingFrames(String text, TextColor[] colors) {
        // Build a ping-pong gradient: forward then reverse (excluding endpoints to avoid stutter)
        List<TextColor> forward = buildGradient(colors, FADING_FRAMES / 2 + 1);
        List<TextColor> pingpong = new ArrayList<>(FADING_FRAMES);
        pingpong.addAll(forward.subList(0, forward.size() - 1));
        List<TextColor> reversed = new ArrayList<>(forward.subList(1, forward.size() - 1));
        java.util.Collections.reverse(reversed);
        pingpong.addAll(reversed);

        List<Component> frames = new ArrayList<>(pingpong.size());
        for (TextColor color : pingpong) {
            Component line = Component.text(text).color(color)
                    .decoration(TextDecoration.ITALIC, false);
            frames.add(line);
        }
        return frames;
    }

    // --- Color math ---

    private static List<TextColor> buildGradient(TextColor[] colors, int steps) {
        List<TextColor> gradient = new ArrayList<>(steps);
        if (colors.length == 1 || steps == 1) {
            for (int i = 0; i < steps; i++) gradient.add(colors[0]);
            return gradient;
        }
        int segments = colors.length - 1;
        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1) * segments;
            int seg = Math.min((int) t, segments - 1);
            gradient.add(lerp(colors[seg], colors[seg + 1], t - seg));
        }
        return gradient;
    }

    private static TextColor lerp(TextColor a, TextColor b, float t) {
        int r = (int) (a.red()   + (b.red()   - a.red())   * t);
        int g = (int) (a.green() + (b.green() - a.green()) * t);
        int bl = (int) (a.blue() + (b.blue()  - a.blue())  * t);
        return TextColor.color(clamp(r), clamp(g), clamp(bl));
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    private static String colorKey(TextColor[] colors) {
        return Arrays.stream(colors).map(TextColor::asHexString).collect(Collectors.joining(","));
    }
}
