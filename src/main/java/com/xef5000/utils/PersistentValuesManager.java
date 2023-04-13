package com.xef5000.utils;

import com.xef5000.FrogMod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class PersistentValuesManager {
    private final File persistentValuesFile;
    private PersistentValues persistentValues = new PersistentValues();
    private static final ReentrantLock SAVE_LOCK = new ReentrantLock();

    public static class PersistentValues {
        private Map<String, Integer> cropSpeed = new HashMap<>();
        private Map<String, Integer> milestone = new HashMap<>();

        public Map<String, Integer> getMilestone() {
            return milestone;
        }

        public Map<String, Integer> getMilestoneProgression() {
            return milestoneProgression;
        }

        private Map<String, Integer> milestoneProgression = new HashMap<>();

        public Map<String, Integer> getCropSpeed() {
            return cropSpeed;
        }

        public void setCropSpeed(Map<String, Integer> cropSpeed) {
            this.cropSpeed = cropSpeed;
        }

        public void setMilestone(Map<String, Integer> milestone) {
            this.milestone = milestone;
        }

        public void setMilestoneProgression(Map<String, Integer> milestoneProgression) {
            this.milestoneProgression = milestoneProgression;
        }
    }

    public PersistentValuesManager(File configDir) {
        this.persistentValuesFile = new File(configDir.getAbsolutePath() + "/frogmod_persistent.cfg");
    }

    public void loadValues() {
        if (persistentValuesFile.exists()) {

            try (FileReader reader = new FileReader(persistentValuesFile)) {
                persistentValues = FrogMod.INSTANCE.getGson().fromJson(reader, PersistentValues.class);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            saveValues();
        }
    }

    public void saveValues() {
        FrogMod.runAsync(() -> {
            if (!SAVE_LOCK.tryLock()) {
                return;
            }

            //System.out.println("[FrogMod] Saving persistent values");

            try {
                //noinspection ResultOfMethodCallIgnored
                persistentValuesFile.createNewFile();

                try (FileWriter writer = new FileWriter(persistentValuesFile)) {
                    FrogMod.INSTANCE.getGson().toJson(persistentValues, writer);
                }
            } catch (Exception ex) {
                System.out.println("[FrogMod] Error saving persistent values.");
                ex.printStackTrace();
            }

            //System.out.println("[FrogMod] Persistent Values Saved");

            SAVE_LOCK.unlock();
        });
    }

    public PersistentValues getPersistentValues() {
        return persistentValues;
    }
}
