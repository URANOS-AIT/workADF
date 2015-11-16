package adf.agent.info;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PreDatas
{
	public Map<String, Integer> intValues;
	public Map<String, Double> doubleValues;
	public Map<String, String> stringValues;
	public Map<String, Integer> idValues;

	public Map<String, List<Integer>> intLists;
	public Map<String, List<Double>> doubleLists;
	public Map<String, List<String>> stringLists;
	public Map<String, List<Integer>> idLists;

	public boolean isReady;

	public PreDatas()
	{
		this.intValues = new HashMap<>();
		this.doubleValues = new HashMap<>();
		this.stringValues = new HashMap<>();
		this.idValues = new HashMap<>();
		this.intLists = new HashMap<>();
		this.doubleLists = new HashMap<>();
		this.stringLists = new HashMap<>();
		this.idLists = new HashMap<>();
		this.isReady = false;
	}

	public PrecomputeDatas copy()
	{
		PrecomputeDatas precomputeDatas = new PrecomputeDatas();
		precomputeDatas.intValues = new HashMap<>(this.intValues);
		precomputeDatas.doubleValues = new HashMap<>(this.doubleValues);
		precomputeDatas.stringValues = new HashMap<>(this.stringValues);
		precomputeDatas.idValues = new HashMap<>(this.idValues);
		precomputeDatas.intLists = new HashMap<>(this.intLists);
		precomputeDatas.doubleLists = new HashMap<>(this.doubleLists);
		precomputeDatas.stringLists = new HashMap<>(this.stringLists);
		precomputeDatas.idLists = new HashMap<>(this.idLists);
		precomputeDatas.isReady = this.isReady;
		return precomputeDatas;
	}
}
