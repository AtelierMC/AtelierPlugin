package tech.ateliermc.atelier.common.hologram;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.HashMap;

public class AtelierHologram {
    public static HashMap<String, Data> hologramMap;
    public static HashMap<Data, AtelierHologramEntity> entityMap;

    public static File saveFile;

    static {
        hologramMap = new HashMap<>();
        entityMap = new HashMap<>();

        saveFile = new File(String.valueOf(FabricLoader.getInstance().getConfigDir()), "holograms.dat");
    }

    @SuppressWarnings("unchecked")
    public static void load() {
        if (!saveFile.exists())
            return;

        try {
            FileInputStream fis = new FileInputStream(saveFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            hologramMap = (HashMap<String, Data>) ois.readObject();

            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            saveFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(saveFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hologramMap);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Data implements Serializable {
        @Serial
        private static final long serialVersionUID = -5210896940322396373L;

        public double x;
        public double y;
        public double z;

        public String[] lines;

        public String world;

        @Override
        public String toString() {
            return String.format("HologramData{%s,%s,%s,%s}", x, y, z, world);
        }
    }
}
