import com.smartpantry.data.SeedData;
import com.smartpantry.logic.*;
import java.time.LocalDate;
import java.util.*;

public class EngineTest {
    static int pass = 0, fail = 0;
    static void t(String n, boolean c) { if (c) pass++; else { fail++; System.out.println("FAIL " + n); } }

    static class S implements Stock {
        long id; String name, unit, exp; double q;
        S(long id, String name, double q, String unit, String exp) { this.id = id; this.name = name; this.q = q; this.unit = unit; this.exp = exp; }
        public long getId() { return id; } public String getName() { return name; } public double getQuantity() { return q; }
        public String getUnit() { return unit; } public String getExpiryDate() { return exp; }
    }
    static class Rq implements Requirement {
        String n, u; double q; Rq(SeedData.Ing i) { n = i.name; q = i.quantity; u = i.unit; }
        Rq(String n, double q, String u) { this.n = n; this.q = q; this.u = u; }
        public String getName() { return n; } public double getQuantity() { return q; } public String getUnit() { return u; }
    }
    static List<Rq> reqs(SeedData.SeedRecipe r) { List<Rq> l = new ArrayList<>(); for (SeedData.Ing i : r.ingredients) l.add(new Rq(i)); return l; }

    public static void main(String[] a) {
        LocalDate today = LocalDate.of(2026, 9, 20);
        t("tomatoes", IngredientNormalizer.normalize("Tomatoes").equals("tomato"));
        t("potatoes", IngredientNormalizer.normalize("potatoes").equals("potato"));
        t("fresh large", IngredientNormalizer.normalize("Fresh large tomatoes").equals("tomato"));
        t("spaghetti", IngredientNormalizer.normalize("Spaghetti").equals("pasta"));
        t("evoo", IngredientNormalizer.normalize("extra virgin olive oil").equals("olive oil"));
        t("cookie", IngredientNormalizer.normalize("cookie").equals(IngredientNormalizer.normalize("cookies")));
        t("hummus", IngredientNormalizer.normalize("hummus").equals("hummus"));
        t("parens", IngredientNormalizer.normalize("Tomatoes (cherry)").equals("tomato"));
        t("chilli", IngredientNormalizer.normalize("Chilli flakes").equals(IngredientNormalizer.normalize("chili flake")));

        List<S> pantry = new ArrayList<>(); long id = 1;
        for (SeedData.DemoItem d : SeedData.DEMO_PANTRY)
            pantry.add(new S(id++, d.name, d.quantity, d.unit, d.expiryOffsetDays == null ? null : today.plusDays(d.expiryOffsetDays).toString()));
        StringBuilder ready = new StringBuilder(), almost = new StringBuilder();
        int rid = 1;
        for (SeedData.SeedRecipe r : SeedData.RECIPES) {
            MatchResult m = RecipeMatcher.evaluate(rid, reqs(r), pantry, true, today, 3);
            if (m.isReady()) ready.append(ready.length() > 0 ? "," : "").append(rid);
            if (m.isAlmostThere()) almost.append(almost.length() > 0 ? "," : "").append(rid);
            rid++;
        }
        System.out.println("READY " + ready + " | ALMOST " + almost);
        t("ready set", ready.toString().equals("1,2,5,6,8,11,13")); t("almost set", almost.toString().equals("3,4,15"));
        t("20 recipes", SeedData.RECIPES.size() == 20);

        // remove cheddar -> 1, 11, 13 vanish
        List<S> noCheese = new ArrayList<>(pantry); noCheese.removeIf(s -> s.name.equals("Cheddar cheese"));
        t("cheddar removal", !RecipeMatcher.evaluate(1, reqs(SeedData.RECIPES.get(0)), noCheese, true, today, 3).isReady()
                && !RecipeMatcher.evaluate(11, reqs(SeedData.RECIPES.get(10)), noCheese, true, today, 3).isReady()
                && RecipeMatcher.evaluate(2, reqs(SeedData.RECIPES.get(1)), noCheese, true, today, 3).isReady());
        // exact quantity boundary
        SeedData.SeedRecipe chick = SeedData.RECIPES.get(4);
        for (double[] c : new double[][]{{300, 1}, {299, 0}}) {
            List<S> p = new ArrayList<>(Arrays.asList(new S(1, "chicken breast", c[0], "g", null), new S(2, "rice", 200, "g", null),
                    new S(3, "onion", 1, "pcs", null), new S(4, "garlic", 2, "pcs", null), new S(5, "soy sauce", 2, "tbsp", null)));
            t("boundary " + c[0], RecipeMatcher.evaluate(5, reqs(chick), p, true, today, 3).isReady() == (c[1] == 1));
        }
        // unit differences
        for (Object[] c : new Object[][]{{30.0, "ml", true}, {29.0, "ml", false}, {0.25, "cup", true}, {1.0, "l", true}}) {
            List<S> p = new ArrayList<>(Arrays.asList(new S(1, "Chicken", 150, "g", null), new S(6, "chicken breast", 150, "g", null), new S(2, "rice", 0.2, "kg", null),
                    new S(3, "onions", 1, "pcs", null), new S(4, "garlic", 2, "pcs", null), new S(5, "soy sauce", (Double) c[0], (String) c[1], null)));
            t("unit " + c[0] + c[1], RecipeMatcher.evaluate(5, reqs(chick), p, true, today, 3).isReady() == (Boolean) c[2]);
        }
        // mass covers count
        List<Rq> tp = reqs(SeedData.RECIPES.get(2));
        List<S> p3 = Arrays.asList(new S(1, "pasta", 200, "g", null), new S(2, "tomatoes", 500, "g", null), new S(3, "onion", 1, "pcs", null),
                new S(4, "garlic", 2, "pcs", null), new S(5, "olive oil", 2, "tbsp", null), new S(6, "salt", 1, "tsp", null));
        t("mass covers count", RecipeMatcher.evaluate(3, tp, p3, true, today, 3).isReady());
        List<S> p4 = Arrays.asList(new S(1, "pasta", 200, "g", null), new S(2, "tomatoes", 500, "g", null), new S(3, "onion", 1, "cup", null),
                new S(4, "garlic", 2, "pcs", null), new S(5, "olive oil", 2, "tbsp", null), new S(6, "salt", 1, "tsp", null));
        MatchResult m4 = RecipeMatcher.evaluate(3, tp, p4, true, today, 3);
        t("mismatch not ready", !m4.isReady() && m4.firstProblem().status == MatchResult.Status.UNIT_MISMATCH);
        // expiry
        List<S> exp = new ArrayList<>(pantry); exp.replaceAll(s -> s.name.equals("Cheddar cheese") ? new S(s.id, s.name, s.q, s.unit, today.minusDays(1).toString()) : s);
        t("expired excluded", !RecipeMatcher.evaluate(11, reqs(SeedData.RECIPES.get(10)), exp, true, today, 3).isReady());
        t("expired allowed off", RecipeMatcher.evaluate(11, reqs(SeedData.RECIPES.get(10)), exp, false, today, 3).isReady());
        t("empty pantry", RecipeMatcher.evaluate(1, reqs(SeedData.RECIPES.get(0)), new ArrayList<S>(), true, today, 3).missingCount() == 4);
        t("bad unit safe", !RecipeMatcher.evaluate(1, reqs(SeedData.RECIPES.get(0)), Arrays.asList(new S(1, "egg", 3, "xx", null)), true, today, 3).isReady());
        // cooking
        Map<Long, Double> plan = RecipeMatcher.planCooking(reqs(SeedData.RECIPES.get(0)), pantry, true, today);
        t("cook eggs", plan.get(1L) == 3.0); t("cook butter", plan.get(4L) == 240.0); t("cook tomato", plan.get(2L) == 2.0);
        List<S> two = Arrays.asList(new S(1, "egg", 2, "pcs", "2026-09-25"), new S(2, "egg", 4, "pcs", "2026-09-21"));
        Map<Long, Double> pf = RecipeMatcher.planCooking(Arrays.asList(new Rq("eggs", 3, "pcs")), two, true, today);
        t("FEFO", pf.get(2L) == 1.0 && !pf.containsKey(1L));
        Map<Long, Double> del = RecipeMatcher.planCooking(Arrays.asList(new Rq("chicken", 300, "g")), Arrays.asList(new S(1, "chicken breast", 300, "g", null)), true, today);
        t("cook deletes", del.get(1L) == 0.0);
        t("days", RecipeMatcher.daysUntilExpiry("2026-09-23", today) == 3 && RecipeMatcher.daysUntilExpiry(null, today) == null);
        System.out.println(pass + " passed, " + fail + " failed");
        System.exit(fail == 0 ? 0 : 1);
    }
}
