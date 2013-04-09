package dlf.refactoring.enums.maps;

import java.util.HashMap;

import com.google.common.collect.LinkedHashMultimap;

import dlf.refactoring.enums.ConditionType;
import dlf.refactoring.enums.InputType;

public class InputConditionType2Map {
	
	private final LinkedHashMultimap<InputType,ConditionType> map;
	
	private InputConditionType2Map()
	{
		this.map = LinkedHashMultimap.create();
		map.put(InputType.NEW_NAME, ConditionType.NAME_COLLISION);
	}
	
	private static InputConditionType2Map instance;
	
	public static InputConditionType2Map getInstance()
	{
		if(instance == null)
		{
			instance = new InputConditionType2Map();
		}
		return instance;
	}
	
	public boolean isExisting(InputType it, ConditionType ct)
	{
		return map.containsEntry(it, ct);
	}
}
