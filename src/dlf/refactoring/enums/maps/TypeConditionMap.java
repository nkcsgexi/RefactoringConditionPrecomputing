package dlf.refactoring.enums.maps;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import dlf.refactoring.enums.ConditionType;
import dlf.refactoring.enums.RefactoringType;

public class TypeConditionMap {

	private final Multimap<RefactoringType, ConditionType> map;
	
	private TypeConditionMap()
	{
		this.map = LinkedHashMultimap.create();
		map.put(RefactoringType.RENAME_CLASS, ConditionType.NAME_COLLISION);
		map.put(RefactoringType.RENAME_METHOD, ConditionType.NAME_COLLISION);
		map.put(RefactoringType.EXTRACT_METHOD, ConditionType.EXTRACTABLE_STATEMENTS);
		map.put(RefactoringType.PULL_UP_METHOD, ConditionType.PULLABLE_METHOD);
		map.put(RefactoringType.PUSH_DOWN_METHOD, ConditionType.PUSHABLE_METHOD);
	}
	
	private static TypeConditionMap instance;
	
	public static TypeConditionMap getInstance()
	{
		if(instance == null)
		{
			instance = new TypeConditionMap();
		}
		return instance;
	}
	
	public boolean isExisting(RefactoringType rt, ConditionType ct)
	{
		return map.containsEntry(rt, ct);
	}
}
