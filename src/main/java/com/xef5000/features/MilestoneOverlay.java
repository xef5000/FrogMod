package com.xef5000.features;

import com.xef5000.FrogMod;
import com.xef5000.utils.LocationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class MilestoneOverlay {

    private static final MilestoneOverlay INSTANCE = new MilestoneOverlay();
    private long lastLongUpdate = 0;
    private long lastVeryLongUpdate = 0;

    public static ArrayList<Integer> cropMilestones = new ArrayList<>(Arrays.asList(0, 100, 150, 250, 500, 1500, 2500, 5000, 5000, 10000, 25000, 25000, 25000, 30000, 70000, 100000, 200000, 250000, 20000, 500000, 1000000, 1500000, 2000000, 3000000, 4000000, 7000000, 10000000, 20000000, 25000000, 25000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 50000000, 100000000));
    public static String[] currentCrop = null;
    public static String editGuiString = EnumChatFormatting.GOLD + "Crop Milestone:";
    private final LinkedList<Integer> counterQueue = new LinkedList<>();
    private int counterVeryLast = -1;
    private int counterLast = -1;
    private int counter = -1;
    private float cropsPerSecondLast = 0;
    private float cropsPerSecond = 0;
    private float cropsPerTenSeconds = 0;
    private float cropsPerTenSecondsLast = 0;
    private long lastUpdate = -1;
    private int tick = 0;
    private boolean longUpdate = false;
    private boolean veryLongUpdate = false;
    private boolean isFarming = false;
    private int bestCPM = 0;

    private Map<String, Integer> cropSpeed = new HashMap<>();
    private Map<String, Integer> milestones = new HashMap<>();
    private Map<String, Integer> milestoneProgression = new HashMap<>();
    private Map<String, Integer> defaultCropSpeed = new HashMap<String, Integer>() {{
        put("Potato", 0);
        put("Wheat", 0);
        put("Carrot", 0);
        put("Pumpkin", 0);
        put("Sugar Cane", 0);
        put("Melon", 0);
        put("Cactus", 0);
        put("Cocoa Beans", 0);
        put("Mushroom", 0);
        put("Nether Wart", 0);
    }};

    private Map<String, Integer> defaultMilestones = new HashMap<String, Integer>() {{
        put("Potato", 1);
        put("Wheat", 1);
        put("Carrot", 1);
        put("Pumpkin", 1);
        put("Sugar Cane", 1);
        put("Melon", 1);
        put("Cactus", 1);
        put("Cocoa Beans", 1);
        put("Mushroom", 1);
        put("Nether Wart", 1);
    }};

    private Map<String, Integer> defaultMilestoneProgression = new HashMap<String, Integer>() {{
        put("Potato", 0);
        put("Wheat", 0);
        put("Carrot", 0);
        put("Pumpkin", 0);
        put("Sugar Cane", 0);
        put("Melon", 0);
        put("Cactus", 0);
        put("Cocoa Beans", 0);
        put("Mushroom", 0);
        put("Nether Wart", 0);
    }};





    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (!FrogMod.INSTANCE.getFrogModConfig().milestoneDisplay) return;


        if (FrogMod.mc.thePlayer == null) return;
        if (LocationManager.getInstance().getLocation() != null && LocationManager.getInstance().getLocation().equals("garden")) {
            tick++;

            checkForEmptyValues();
            Collection<NetworkPlayerInfo> players = Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfoMap();
            //  Milestone: Wheat 20: 68.3%
            for (NetworkPlayerInfo info : players) {
                String name = FrogMod.mc.ingameGUI.getTabList().getPlayerName(info);
                if (name.contains("Milestone:")) {
                    currentCrop = new String[6];
                    name = name.replaceAll("§[f|F|r]", ""); // Output: Milestone: Wheat 20: 68.3%
                    name = EnumChatFormatting.getTextWithoutFormattingCodes(name);
                    String crop = name.split(" ")[2];
                    int offset = 0;
                    if (crop.equals("Cocoa") || crop.equals("Sugar") || crop.equals("Nether")) {
                        if (crop.equals("Cocoa")) crop = "Cocoa Beans";
                        if (crop.equals("Sugar")) crop = "Sugar Cane";
                        if (crop.equals("Nether")) crop = "Nether Wart";
                        offset = 1;
                    }

                    if (crop.equals("N/A")) return; // Visiting another player's garden. Need this to prevent crash

                    String level = name.split(" ")[3 + offset].replaceAll(":", "");
                    String percent = name.split(" ")[4 + offset].replaceAll("%", "");

                    int levelInt = Integer.parseInt(level);
                    float floatPercentage = Float.parseFloat(percent);
                    int progression = (int) (cropMilestones.get(levelInt + 1) * (floatPercentage / 100));
                    int nextMilestone = cropMilestones.get(levelInt + 1);
                    int minutesLeft = (int) ((nextMilestone - progression) / (bestCPM * 0.90));

                    milestones.remove(crop);
                    milestones.put(crop, nextMilestone);
                    milestoneProgression.remove(crop);
                    milestoneProgression.put(crop, progression);


                    currentCrop[0] = "§6Crop Milestone";
                    currentCrop[1] = "§f" + crop;
                    currentCrop[2] = "§7Progress to Tier " + (levelInt + 1) + ":";
                    currentCrop[3] = "§e" + String.format("%,d",progression) + "/" + String.format("%,d", nextMilestone);
                    if (counter >= 0) {
                        if (cropsPerSecondLast == cropsPerSecond && cropsPerSecond <= 0) {
                            if (cropsPerSecondLast >=1 ) { }
                            currentCrop[4] = "§7Crops/minute: §eN/A";
                        } else if(cropsPerSecondLast >= 1 && cropsPerSecond <= 0){ // Stopped farming
                            isFarming = false;
                            if (bestCPM > cropSpeed.get(crop)){
                                cropSpeed.remove(crop);
                                cropSpeed.put(crop, bestCPM);
                                savePersistentValues();
                            }
                            //bestCPM = 0;

                            //FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText("Best cpm: " + bestCPM));
                        } else if (cropsPerSecondLast <= 0 && cropsPerSecond >= 1) { // Started farming
                            isFarming = true;
                            bestCPM = cropSpeed.get(crop);
                        } else {
                            isFarming = true;
                            float cpsInterp = interp(cropsPerSecond, cropsPerSecondLast);
                            //float cpsTenInterp = interp(cropsPerSecond, cropsPerTenSecondsLast);
                            currentCrop[4] = "§7Crops/minute: §e" + String.format("%,.2f", cpsInterp * 60);
                        }
                    }
                    currentCrop[5] = "§7in §b" + String.format("%dh%02dm", minutesLeft/60, minutesLeft%60);

                    //FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText("Crop: " + crop + ", Level: " + level + ", Percent: " + percent)); // Output: Crop: Wheat, Level: 20:, Percent: 68.3%
                    break; // To improve performances ;)
                }
            }




            longUpdate = false;
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastLongUpdate > 1000) {
                longUpdate = true;
                lastLongUpdate = currentTime;
            }
            if (!longUpdate) return;
            lastUpdate = System.currentTimeMillis();
            counterLast = counter;
            counter = -1;

            ItemStack stack = FrogMod.mc.thePlayer.getHeldItem();
            if (stack != null && stack.hasTagCompound()) {
                NBTTagCompound tag = stack.getTagCompound();

                if (tag.hasKey("ExtraAttributes", 10)) {
                    NBTTagCompound ea = tag.getCompoundTag("ExtraAttributes");

                    if (ea.hasKey("mined_crops", 99)) {
                        counter = ea.getInteger("mined_crops");
                        counterQueue.add(0, counter);
                    } else if (ea.hasKey("farmed_cultivating", 99)) {
                        counter = ea.getInteger("mined_crops");
                        counterQueue.add(0, counter);
                    }
                }
            }



            while (counterQueue.size() >= 4) {
                counterQueue.removeLast();
            }

            if (counterQueue.isEmpty()) {
                cropsPerSecond = -1;
                cropsPerSecondLast = 0;
            } else {
                cropsPerSecondLast = cropsPerSecond;
                int last = counterQueue.getLast();
                int first = counterQueue.getFirst();

                cropsPerSecond = counter - counterLast;
                if (isFarming && cropsPerSecond * 60 > bestCPM) bestCPM = (int) cropsPerSecond * 60;

            }
        } else {currentCrop = new String[6];}
/*
        veryLongUpdate = false;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastVeryLongUpdate > 10000) {
            veryLongUpdate = true;
            lastVeryLongUpdate = currentTime;
        }
        if (!veryLongUpdate) return;

        if (counterQueue.isEmpty()) {
            cropsPerTenSeconds = -1;
            cropsPerTenSecondsLast = 0;
        } else {
            cropsPerTenSecondsLast = cropsPerTenSeconds;
            if (counterVeryLast == -1) {
                counterVeryLast = counter;
                return;
            }
            cropsPerTenSeconds = counter - counterVeryLast;
            counterVeryLast = counter;
            FrogMod.mc.thePlayer.addChatMessage(new ChatComponentText("Made " + cropsPerTenSeconds + " in the past 10s (" + cropsPerTenSeconds / 10 + " per second, " + cropsPerTenSeconds * 6 +" per minute)"));
        }

 */


    }

    private float interp(float now, float last) {
        float interp = now;
        if (last >= 0 && last != now) {
            float factor = (System.currentTimeMillis() - lastUpdate) / 1000f;
            factor = clampZeroOne(factor);
            interp = last + (now - last) * factor;
        }
        return interp;
    }


    public static float clampZeroOne(float f) {
        return Math.max(0, Math.min(1, f));
    }

    public void savePersistentValues() {
        FrogMod.INSTANCE.getPersistentValuesManager().getPersistentValues().setCropSpeed(cropSpeed);
        FrogMod.INSTANCE.getPersistentValuesManager().getPersistentValues().setMilestone(milestones);
        FrogMod.INSTANCE.getPersistentValuesManager().getPersistentValues().setMilestoneProgression(milestoneProgression);
        FrogMod.INSTANCE.getPersistentValuesManager().saveValues();
    }

    public void resetCropSpeed() {
        cropSpeed.clear();
        cropSpeed = defaultCropSpeed;
        savePersistentValues();
    }

    public HashMap<String, Integer> getTopCrops() {
        Map<String, Integer> cropSpeeds = FrogMod.INSTANCE.getPersistentValuesManager().getPersistentValues().getCropSpeed();
        HashMap<String, Integer> newCropSpeeds = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : cropSpeeds.entrySet()) {
            list.add(entry.getValue());

        }
        list.sort(new Comparator<Integer>() {
            public int compare(Integer str, Integer str1) {
                return (str).compareTo(str1);
            }
        });

        for (Integer str : list) {
            for (Map.Entry<String, Integer> entry : cropSpeeds.entrySet()) {
                if (entry.getValue().equals(str)) {
                    newCropSpeeds.put(entry.getKey(), str);
                }
            }
        }
        return newCropSpeeds;

    }

    public void checkForEmptyValues() {
        boolean changed = false;
        if (cropSpeed.isEmpty()) cropSpeed = FrogMod.INSTANCE.getPersistentValuesManager().getPersistentValues().getCropSpeed();
        if (cropSpeed.isEmpty()) {
            cropSpeed = defaultCropSpeed;
            changed = true;
        }
        if (milestones.isEmpty()) milestones = FrogMod.INSTANCE.getPersistentValuesManager().getPersistentValues().getMilestone();
        if (milestones.isEmpty()) {
            milestones = defaultMilestones;
            changed = true;
        }
        if (milestoneProgression.isEmpty()) milestoneProgression = FrogMod.INSTANCE.getPersistentValuesManager().getPersistentValues().getMilestoneProgression();
        if (milestoneProgression.isEmpty()) {
            milestoneProgression = defaultMilestoneProgression;
            changed = true;
        }
        if (changed) savePersistentValues();
    }

    public static MilestoneOverlay getInstance() {return INSTANCE;}
}
