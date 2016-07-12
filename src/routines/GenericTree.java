package routines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Helper class for handling tree structured data
 * @author jan.lolling@cimt-ag.de
 *
 * @param <T> generic class
 */
public abstract class GenericTree<T> {
	
	private Map<T, T> childParentMap = new HashMap<T, T>();
	private T root = null;
	private List<T> allowedParents = new ArrayList<T>();
	
	/**
	 * add a child-parent relation
	 * @param child
	 * @param parent
	 * @return the previous parent if a child already exists 
	 */
	public T add(T child, T parent) {
		if (child == null) {
			if (root == null) {
				root = parent;
				return null;
			} else {
				throw new IllegalArgumentException("Child cannot be null in case of root is already set:" + root);
			}
		} else {
			return childParentMap.put(child, parent);
		}
	}
	
	/**
	 * add a possible parent to the list of allowed parents
	 * @param parent
	 * @return
	 */
	public boolean addAllowedParent(T parent) {
		return allowedParents.add(parent);
	}
	
	/**
	 * returns a list of T which building the nodes from child to root
	 * @param child
	 * @return list of parents excluding root
	 */
	public List<T> collectParents(T child) {
		List<T> parents = new ArrayList<T>();
		collectParents(child, parents);
		return parents;
	}
	
	private void collectParents(T child, List<T> parents) {
		T parent = childParentMap.get(child);
		if (parent != null) {
			parents.add(parent);
			collectParents(parent, parents);
		}
	}
	
	/**
	 * returns the parent and check the parent if it is enabled.
	 * A parent can be any node in the tree and is not necessarily the parent of the
	 * child or the root.
	 * @param child
	 * @param checkForPossibleParents
	 * @return the parent
	 */
	public T getParent(T child, boolean checkForPossibleParents) {
		if (checkForPossibleParents) {
			return getParent(child, allowedParents);
		} else {
			return getParent(child, null);
		}
	}
	
	/**
	 * for a child it retrieves the parent what is mentioned in a list of possible parents
	 * this helps to find parent child relations between a list of parents and one child.
	 * @param child 
	 * @param listPossibleParents list of parents to check
	 * @return the parent what is in the list of possible parents and is part of the path between child and root 
	 */
	public T getParent(T child, List<T> listPossibleParents) {
		final List<T> listParents = collectParents(child);
		if (listPossibleParents != null && listPossibleParents.isEmpty() == false) {
			for (T p : listParents) {
				for (T pp : listPossibleParents) {
					if (p.equals(pp)) {
						return p;
					}
				}
			}
			return null;
		} else {
			if (listParents.isEmpty()) {
				return child;
			} else {
				int pos = listParents.size() - 1;
				return listParents.get(pos);
			}
		}
	}

}
