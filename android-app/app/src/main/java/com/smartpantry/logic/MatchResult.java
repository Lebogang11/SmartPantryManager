package com.smartpantry.logic;

import java.util.ArrayList;
import java.util.List;

/** Outcome of checking one recipe against the pantry, including a line-by-line explanation. */
public class MatchResult {

    public enum Status { OK, MISSING, SHORT, UNIT_MISMATCH }

    /** One recipe ingredient and whether the pantry covers it. */
    public static class Line {
        public final String name;
        public final String need;      // e.g. "200 g"
        public final String have;      // e.g. "150 g + 100 g", empty when absent
        public final Status status;

        Line(String name, String need, String have, Status status) {
            this.name = name; this.need = need; this.have = have; this.status = status;
        }

        public boolean isOk() { return status == Status.OK; }
    }

    public final long recipeId;
    public final List<Line> lines = new ArrayList<>();
    /** Names of pantry items used by this recipe that expire soon (used to rank suggestions). */
    public final List<String> expiringUsed = new ArrayList<>();

    MatchResult(long recipeId) { this.recipeId = recipeId; }

    public int missingCount() {
        int n = 0;
        for (Line l : lines) if (!l.isOk()) n++;
        return n;
    }

    /** True only when EVERY ingredient is present in at least the required quantity. */
    public boolean isReady() { return missingCount() == 0; }

    /** Recipes missing exactly one ingredient (used by the optional "Almost there" list). */
    public boolean isAlmostThere() { return missingCount() == 1; }

    /** The first ingredient that is not covered, or null. */
    public Line firstProblem() {
        for (Line l : lines) if (!l.isOk()) return l;
        return null;
    }
}
