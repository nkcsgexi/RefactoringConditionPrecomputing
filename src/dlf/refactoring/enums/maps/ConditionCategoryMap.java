package dlf.refactoring.enums.maps;

import java.util.HashMap;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import dlf.refactoring.enums.ConditionCategory;
import dlf.refactoring.enums.ConditionType;

public class ConditionCategoryMap {
	private final LinkedHashMultimap<Object,Object> map;
	
	public ConditionCategoryMap()
	{
		this.map = LinkedHashMultimap.create();
		map.put(ConditionType.EXTRACTABLE_STATEMENTS, ConditionCategory.C1);
		map.put(ConditionType.PULLABLE_METHOD, ConditionCategory.C1);
		map.put(ConditionType.PUSHABLE_METHOD, ConditionCategory.C1);
		map.put(ConditionType.NAME_COLLISION, ConditionCategory.C2);
	}
	
	public boolean isExisting(ConditionType ct, ConditionCategory cc)
	{
		return map.containsEntry(ct, cc);
	}
}
