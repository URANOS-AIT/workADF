package adf.util.precomp;

import rescuecore2.worldmodel.EntityID;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataStorage {

    public static final String DEFAULT_FILE_NAME = "precomp_datas.bin";

    private String fileName;

    private Map<String, Integer> intValues;
    private Map<String, Double> doubleValues;
    private Map<String, String> stringValues;
    private Map<String, EntityID> idValues;

    private Map<String, List<Integer>> intLists;
    private Map<String, List<Double>> doubleLists;
    private Map<String, List<String>> stringLists;
    private Map<String, List<EntityID>> idLists;

    public DataStorage() {
        this(DEFAULT_FILE_NAME);
    }

    public DataStorage(String name) {
        this.fileName = name;
        this.intValues = new HashMap<>();
        this.doubleValues = new HashMap<>();
        this.stringValues = new HashMap<>();
        this.idValues = new HashMap<>();
        this.intLists = new HashMap<>();
        this.doubleLists = new HashMap<>();
        this.stringLists = new HashMap<>();
        this.idLists = new HashMap<>();
    }

    public DataStorage copy() {
        DataStorage ds = new DataStorage(this.fileName);
        ds.intValues = new HashMap<>(this.intValues);
        ds.doubleValues = new HashMap<>(this.doubleValues);
        ds.stringValues = new HashMap<>(this.stringValues);
        ds.idValues = new HashMap<>(this.idValues);
        ds.intLists = new HashMap<>(this.intLists);
        ds.doubleLists = new HashMap<>(this.doubleLists);
        ds.stringLists = new HashMap<>(this.stringLists);
        ds.idLists = new HashMap<>(this.idLists);
        return ds;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int setInteger(String name, int value) {
        return this.intValues.put(name, value);
    }

    public double setDouble(String name, double value) {
        return this.doubleValues.put(name, value);
    }

    public String setString(String name, String value) {
        return this.stringValues.put(name, value);
    }

    public EntityID setEntityID(String name, EntityID value) {
        return this.idValues.put(name, value);
    }

    public List<Integer> setInteger(String name, List<Integer> list) {
        return this.intLists.put(name, list);
    }

    public List<Double> setDouble(String name, List<Double> list) {
        return this.doubleLists.put(name, list);
    }

    public List<String> setString(String name, List<String> list) {
        return this.stringLists.put(name, list);
    }

    public List<EntityID> setEntityID(String name, List<EntityID> list) {
        return this.idLists.put(name, list);
    }

    public int getInteger(String name) {
        return this.intValues.get(name);
    }

    public double getDouble(String name) {
        return this.doubleValues.get(name);
    }

    public String getString(String name) {
        return this.stringValues.get(name);
    }

    public EntityID getEntityID(String name) {
        return this.idValues.get(name);
    }

    public List<Integer> getIntegerList(String name) {
        return this.intLists.get(name);
    }

    public List<Double> getDoubleList(String name) {
        return this.doubleLists.get(name);
    }

    public List<String> getStringList(String name) {
        return this.stringLists.get(name);
    }

    public List<EntityID> getEntityIDList(String name) {
        return this.idLists.get(name);
    }
}
