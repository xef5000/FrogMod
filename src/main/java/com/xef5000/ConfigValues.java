package com.xef5000;

import com.google.gson.*;
import com.xef5000.utils.objects.AnchorPoint;
import com.xef5000.utils.objects.FloatPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.text.WordUtils;

import java.awt.geom.Point2D;
import java.beans.Introspector;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ConfigValues {

    private Map<Feature, Float> guiScales = new EnumMap<>(Feature.class);
    private final Map<Feature, Float> defaultGuiScales = new EnumMap<>(Feature.class);
    private final Map<Feature, AnchorPoint> defaultAnchorPoints = new EnumMap<>(Feature.class);
    private final Map<Feature, FloatPair> defaultCoordinates = new EnumMap<>(Feature.class);
    private final static float GUI_SCALE_MINIMUM = 0.5F;
    private final static float GUI_SCALE_MAXIMUM = 5;

    private final Map<Feature, FloatPair> coordinates = new EnumMap<>(Feature.class);
    private Map<Feature, AnchorPoint> anchorPoints = new EnumMap<>(Feature.class);
    private JsonObject loadedConfig = new JsonObject();
    private final Set<Feature> disabledFeatures = EnumSet.noneOf(Feature.class);

    private final File settingsConfigFile;

    public ConfigValues(File settingsConfigFile) {
        this.settingsConfigFile = settingsConfigFile;
    }

    public void loadValues() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("default.json");
             InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8)) {

            JsonObject defaultValues = FrogMod.INSTANCE.getGson().fromJson(inputStreamReader, JsonObject.class);

            deserializeFeatureFloatCoordsMapFromID(defaultValues, defaultCoordinates, "coordinates");
            deserializeEnumNumberMapFromID(defaultValues, defaultGuiScales, "guiScales", Feature.class, float.class);
            deserializeEnumEnumMapFromIDS(defaultValues, defaultAnchorPoints, "anchorPoints", Feature.class, AnchorPoint.class);
            deserializeFeatureSetFromID(disabledFeatures, "disabledFeatures");
            deserializeEnumNumberMapFromID(guiScales, "guiScales", Feature.class, float.class);
            System.out.println("TEST_TEXT GUI SCALE: " + guiScales.get(Feature.TEST_TEXT));
            for (Feature feature : guiScales.keySet()) System.out.println(feature);


            if(settingsConfigFile.exists()) {
                try (FileReader reader = new FileReader(settingsConfigFile)) {
                    JsonElement fileElement = new JsonParser().parse(reader);

                    if (fileElement == null || fileElement.isJsonNull()) {
                        throw new JsonParseException("File is null!");
                    }
                    loadedConfig = fileElement.getAsJsonObject();
                } catch (JsonParseException | IllegalStateException | IOException ex) {
                    System.out.println("There was an error loading the config. Resetting all settings to default.");
                    System.out.println(ex);
                    return;
                }
            }

        } catch (Exception exception) {
            System.out.println("ah ha - frogmod error");
        }
/*
        try {
            for (Feature feature : Feature.getGuiFeatures()) { // TODO Legacy format from 1.3.4, remove in the future.
                String property = Introspector.decapitalize(WordUtils.capitalizeFully(feature.toString().replace("_", " "))).replace(" ", "");
                String x = property+"X";
                String y = property+"Y";
                if (loadedConfig.has(x)) {
                    coordinates.put(feature, new FloatPair(loadedConfig.get(x).getAsFloat(), loadedConfig.get(y).getAsFloat()));
                    System.out.println("Put a new coord");
                    System.out.println("X/Y: " + coordinates.get(feature).getX() + " " + coordinates.get(feature).getY());
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to deserialize path: coordinates (legacy)");
            System.out.println(ex);
        }
*/

        try {
            System.out.println("Loading coords...");
            if (loadedConfig.has("coordinates")) {
                for (Map.Entry<String, JsonElement> element : loadedConfig.getAsJsonObject("coordinates").entrySet()) {
                    Feature feature = Feature.fromId(Integer.parseInt(element.getKey()));
                    if (feature != null) {
                        JsonArray coords = element.getValue().getAsJsonArray();
                        coordinates.put(feature, new FloatPair(coords.get(0).getAsFloat(), coords.get(1).getAsFloat()));
                        System.out.println("Put a new coord");
                        System.out.println("X/Y: " + coordinates.get(feature).getX() + " " + coordinates.get(feature).getY());
                    } else {
                        System.out.println("[FrogMod] Returned null...");
                    }
                }
            } else {
                System.out.println("Doesn't have coords");
            }
        } catch (Exception ex) {
            System.out.println("Failed to deserialize path: "+ "coordinates");
            System.out.println(ex);
        }


    }



    private Number getNumber(JsonElement jsonElement, Class<? extends Number> numberClass) {
        if (numberClass == byte.class) { return jsonElement.getAsByte();
        } else if (numberClass == short.class) { return jsonElement.getAsShort();
        } else if (numberClass == int.class) { return jsonElement.getAsInt();
        } else if (numberClass == long.class) { return jsonElement.getAsLong();
        } else if (numberClass == float.class) { return jsonElement.getAsFloat();
        } else if (numberClass == double.class) { return jsonElement.getAsDouble(); }

        return null;
    }

    private <E extends Enum<?>, N extends Number> void deserializeEnumNumberMapFromID(JsonObject jsonObject, Map<E, N> map, String path, Class<E> keyClass, Class<N> numberClass) {
        try  {
            if (jsonObject.has(path)) {
                for (Map.Entry<String, JsonElement> element : jsonObject.getAsJsonObject(path).entrySet()) {
                    Method fromId = keyClass.getDeclaredMethod("fromId", int.class);
                    E key = (E)fromId.invoke(null, Integer.parseInt(element.getKey()));
                    if (key != null) {
                        map.put(key, (N)getNumber(element.getValue(), numberClass));
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Little error here from frogmod");
        }
    }

    private <E extends Enum<?>, N extends Number> void deserializeEnumNumberMapFromID(Map<E, N> map, String path, Class<E> keyClass, Class<N> numberClass) {
        deserializeEnumNumberMapFromID(loadedConfig, map, path, keyClass, numberClass);
    }

    private <E extends Enum<?>, F extends Enum<?>> void deserializeEnumEnumMapFromIDS(JsonObject jsonObject, Map<E, F> map, String path, Class<E> keyClass, Class<F> valueClass) {
        try {
            if (jsonObject.has(path)) {
                for (Map.Entry<String, JsonElement> element : jsonObject.getAsJsonObject(path).entrySet()) {

                    Method fromId = keyClass.getDeclaredMethod("fromId", int.class);
                    E key = (E)fromId.invoke(null, Integer.parseInt(element.getKey()));

                    fromId = valueClass.getDeclaredMethod("fromId", int.class);
                    F value = (F)fromId.invoke(null, element.getValue().getAsInt());

                    if (key != null && value != null) {
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to deserialize path: "+ path);
            System.out.println(ex);
        }
    }

    private void deserializeFeatureSetFromID(Collection<Feature> collection, String path) {
        try {
            if (loadedConfig.has(path)) {
                for (JsonElement element : loadedConfig.getAsJsonArray(path)) {
                    Feature feature = Feature.fromId(element.getAsInt());
                    if (feature != null) {
                        collection.add(feature);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to deserialize path: "+ path);
            System.out.println(ex);
        }
    }

    private void deserializeFeatureFloatCoordsMapFromID(Map<Feature, FloatPair> map, String path) {
        deserializeFeatureFloatCoordsMapFromID(loadedConfig, map, path);
    }

    private void deserializeFeatureFloatCoordsMapFromID(JsonObject jsonObject, Map<Feature, FloatPair> map, String path) {
        try {
            if (jsonObject.has(path)) {
                for (Map.Entry<String, JsonElement> element : jsonObject.getAsJsonObject(path).entrySet()) {
                    Feature feature = Feature.fromId(Integer.parseInt(element.getKey()));
                    if (feature != null) {
                        JsonArray coords = element.getValue().getAsJsonArray();
                        map.put(feature, new FloatPair(coords.get(0).getAsFloat(), coords.get(1).getAsFloat()));
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to deserialize path: "+ path);
            System.out.println(ex);
        }
    }

    public float getGuiScale(Feature feature) {
        return getGuiScale(feature, false);
    }

    public float getGuiScale(Feature feature, boolean denormalized) {
        float value = 1f;
        if (guiScales.containsKey(feature)) {
            value = guiScales.get(feature);
        }
        if (denormalized) {
            value = denormalizeScale(value);
        }
        return value;
    }

    public float getActualX(Feature feature) {
        int maxX = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
        return getAnchorPoint(feature).getX(maxX) + getRelativeCoords(feature).getX();
    }

    public float getActualY(Feature feature) {
        int maxY = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
        return getAnchorPoint(feature).getY(maxY) + getRelativeCoords(feature).getY();
    }

    public FloatPair getRelativeCoords(Feature feature) {
        if (coordinates.containsKey(feature)) {
            return coordinates.get(feature);
        } else {
            if (coordinates.containsKey(feature)) {
                return coordinates.get(feature);
            } else {
                return new FloatPair(0,0);
            }
        }
    }
    public AnchorPoint getAnchorPoint(Feature feature) {
        return anchorPoints.getOrDefault(feature, defaultAnchorPoints.getOrDefault(feature, AnchorPoint.BOTTOM_MIDDLE));
    }


    public static float denormalizeScale(float value) {
        return snapNearDefaultValue(ConfigValues.GUI_SCALE_MINIMUM + (ConfigValues.GUI_SCALE_MAXIMUM - ConfigValues.GUI_SCALE_MINIMUM) *
                MathHelper.clamp_float(value, 0.0F, 1.0F));
    }
    public static float snapNearDefaultValue(float value) {
        if (value != 1 && value > 1-0.05 && value < 1+0.05) {
            return 1;
        }

        return value;
    }
    public void setGuiScale(Feature feature, float scale) {
        guiScales.put(feature, scale);
    }

    public static float normalizeValueNoStep(float value) {
        return MathHelper.clamp_float((snapNearDefaultValue(value) - ConfigValues.GUI_SCALE_MINIMUM) /
                (ConfigValues.GUI_SCALE_MAXIMUM - ConfigValues.GUI_SCALE_MINIMUM), 0.0F, 1.0F);
    }

    public void setCoords(Feature feature, float x, float y) {
        if (coordinates.containsKey(feature)) {
            coordinates.get(feature).setX(x);
            coordinates.get(feature).setY(y);
        } else {
            coordinates.put(feature, new FloatPair(x, y));
        }
    }

    public boolean isEnabled(Feature feature) {
        return !isDisabled(feature);
    }
    public boolean isDisabled(Feature feature) {
        return disabledFeatures.contains(feature);
    }

    public void setClosestAnchorPoint(Feature feature) {
        float x1 = getActualX(feature);
        float y1 = getActualY(feature);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int maxX = sr.getScaledWidth();
        int maxY = sr.getScaledHeight();
        double shortestDistance = -1;
        AnchorPoint closestAnchorPoint = AnchorPoint.BOTTOM_MIDDLE; // default
        for (AnchorPoint point : AnchorPoint.values()) {
            double distance = Point2D.distance(x1, y1, point.getX(maxX), point.getY(maxY));
            if (shortestDistance == -1 || distance < shortestDistance) {
                closestAnchorPoint = point;
                shortestDistance = distance;
            }
        }
        if (this.getAnchorPoint(feature) == closestAnchorPoint) {
            return;
        }
        float targetX = getActualX(feature);
        float targetY = getActualY(feature);
        float x = targetX-closestAnchorPoint.getX(maxX);
        float y = targetY-closestAnchorPoint.getY(maxY);
        anchorPoints.put(feature, closestAnchorPoint);
        setCoords(feature, x, y);
    }


    public void saveConfig() {
        FrogMod.runAsync(() -> {
            System.out.println("Saving config");

            try {
                settingsConfigFile.createNewFile();

                JsonObject saveConfig = new JsonObject();

                JsonArray jsonArray = new JsonArray();
                for (Feature element : disabledFeatures) {
                    jsonArray.add(new GsonBuilder().create().toJsonTree(element.getId()));
                }
                saveConfig.add("disabledFeatures", jsonArray);

                JsonObject anchorObject = new JsonObject();
                for (Feature feature : Feature.getGuiFeatures()) {
                    anchorObject.addProperty(String.valueOf(feature.getId()), getAnchorPoint(feature).getId());
                }
                saveConfig.add("anchorPoints", anchorObject);
/*
                JsonObject scalesObject = new JsonObject();
                for (Feature feature : guiScales.keySet()) {
                    scalesObject.addProperty(String.valueOf(feature.getId()), guiScales.get(feature));
                }
                saveConfig.add("guiScales", scalesObject);

 */

                JsonObject coordinatesObject = new JsonObject();
                for (Feature feature : coordinates.keySet()) {
                    JsonArray coordinatesArray = new JsonArray();
                    coordinatesArray.add(new GsonBuilder().create().toJsonTree(coordinates.get(feature).getX()));
                    coordinatesArray.add(new GsonBuilder().create().toJsonTree(coordinates.get(feature).getY()));
                    coordinatesObject.add(String.valueOf(feature.getId()), coordinatesArray);
                }
                saveConfig.add("coordinates", coordinatesObject);


                try (FileWriter writer = new FileWriter(settingsConfigFile)) {
                    FrogMod.INSTANCE.getGson().toJson(saveConfig, writer);
                }
            } catch (Exception ex) {
                System.out.println("An error occurred while attempting to save the config!");
                System.out.println(ex);


                System.out.println("Config saved");
            }
        });
    }
}
