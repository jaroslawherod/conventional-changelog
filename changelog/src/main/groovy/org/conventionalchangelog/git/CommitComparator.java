package org.conventionalchangelog.git;

import java.util.Comparator;

import org.ajoberstar.grgit.Commit;

public class CommitComparator implements Comparator<Commit> {

    @Override
    public int compare(Commit o1, Commit o2) {
        final int result = o1.getDate().compareTo(o2.getDate());
        if (result != 0) {
            return -result;
        }
        if(o1.getParentIds() != null &&  o1.getParentIds().contains(o2.getId())){
            return 1;
        }
        if(o2.getParentIds() != null && o2.getParentIds().contains(o1.getId())){
            return -1;
        }
        return 0;
    }
}
