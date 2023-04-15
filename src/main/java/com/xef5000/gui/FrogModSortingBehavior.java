package com.xef5000.gui;

import gg.essential.vigilance.data.Category;
import gg.essential.vigilance.data.SortingBehavior;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.Comparator;

public class FrogModSortingBehavior extends SortingBehavior {

    private static final ArrayList<String> categories = new ArrayList<String>() {{
        add("General");
        add("Dungeons");
        add("Mining");
        add("Render");
        add("Other");
        add("Test");
    }};
    @Override
    public Comparator<Category> getCategoryComparator() {

        return new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return categories.indexOf(o1.getName()) - categories.indexOf(o2.getName());
            }
        };

        /*
        return (o1, o2) -> {
            if (o1.getName().equals("General")) {
                return 0;
            }
            if (o2.getName().equals("General")) {
                return 1;
            } else {

                return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
            }

        };

         */
    }

}
