/* Smart Pantry Manager - strict matching engine (mirrors the Java classes in com.smartpantry.logic) */
const Engine = (() => {
  // ---- Units: every unit belongs to a dimension and has a factor to that dimension's base unit
  const UNITS = {
    g:{dim:'mass',f:1}, kg:{dim:'mass',f:1000},
    ml:{dim:'vol',f:1}, l:{dim:'vol',f:1000}, tsp:{dim:'vol',f:5}, tbsp:{dim:'vol',f:15}, cup:{dim:'vol',f:240},
    pcs:{dim:'count',f:1}, can:{dim:'count',f:1}
  };
  // grams per millilitre - lets "1 tsp salt" be satisfied by "200 g salt"
  const DENSITY = {salt:1.2,sugar:0.85,flour:0.53,butter:0.96,honey:1.4,oat:0.36,rice:0.85,cumin:0.45,
    'curry powder':0.5,'chilli flake':0.35,'olive oil':0.91,'vegetable oil':0.92,'soy sauce':1.2,milk:1.03,
    cream:1.0,yoghurt:1.03,mayonnaise:0.95,water:1.0,lentil:0.85,'vegetable stock':1.0,'coconut milk':1.0};
  // grams per piece - lets "3 pcs tomato" be satisfied by "500 g tomato"
  const PIECE_G = {egg:55,tomato:120,potato:170,onion:150,garlic:5,carrot:80,'bell pepper':160,banana:120,
    avocado:170,lime:65,lemon:90,bread:30,apple:180,'spring onion':15};

  // ---- Name normalisation
  const STOP = new Set(['fresh','large','small','medium','ripe','chopped','diced','sliced','minced','grated',
    'organic','raw','frozen','mature','extra','virgin','plain','natural','whole','a','the','of']);
  const IRREGULAR = {leaves:'leaf',loaves:'loaf',halves:'half',knives:'knife'};
  const ALIASES = {
    spaghetti:'pasta',penne:'pasta',macaroni:'pasta',fusilli:'pasta',linguine:'pasta',tagliatelle:'pasta',
    capsicum:'bell pepper','green pepper':'bell pepper','red pepper':'bell pepper','yellow pepper':'bell pepper','sweet pepper':'bell pepper',
    scallion:'spring onion','green onion':'spring onion',
    yogurt:'yoghurt','greek yogurt':'yoghurt','greek yoghurt':'yoghurt',
    cheddar:'cheddar cheese',
    'cooking oil':'vegetable oil','sunflower oil':'vegetable oil','canola oil':'vegetable oil','rapeseed oil':'vegetable oil',
    'full cream milk':'milk','skim milk':'milk','skimmed milk':'milk','low fat milk':'milk',
    chicken:'chicken breast','chicken fillet':'chicken breast',
    'tinned tomato':'canned tomato','tin tomato':'canned tomato',
    garbanzo:'chickpea','garbanzo bean':'chickpea','chick pea':'chickpea','canned chickpea':'chickpea','tinned chickpea':'chickpea',
    tuna:'canned tuna','tinned tuna':'canned tuna','tinned coconut milk':'coconut milk','canned coconut milk':'coconut milk',
    'chili flake':'chilli flake','red pepper flake':'chilli flake','chilli':'chilli flake',
    courgette:'zucchini',aubergine:'eggplant',coriander:'cilantro',mince:'ground beef'
  };
  function singular(w) {
    if (IRREGULAR[w]) return IRREGULAR[w];
    if (w.length <= 3) return w;
    if (/ies$/.test(w)) return w.slice(0, -3) + 'y';
    if (/ie$/.test(w)) return w.slice(0, -2) + 'y';        // cookie -> cooky (same key as cookies)
    if (/oes$/.test(w)) return w.slice(0, -2);              // tomatoes -> tomato
    if (/(ch|sh|ss|x|z)es$/.test(w)) return w.slice(0, -2); // radishes -> radish
    if (/(ss|us|is)$/.test(w)) return w;                    // hummus, watercress
    if (/s$/.test(w)) return w.slice(0, -1);                // eggs -> egg
    return w;
  }
  function canon(name) {
    const cleaned = String(name || '').normalize('NFD').replace(/[\u0300-\u036f]/g, '')
      .toLowerCase().replace(/\([^)]*\)/g, ' ').replace(/[^a-z\s]/g, ' ');
    const toks = cleaned.split(/\s+/).filter(t => t && !STOP.has(t)).map(singular);
    const key = toks.join(' ');
    return ALIASES[key] || key;
  }

  // ---- Unit maths
  const isUnit = u => Object.prototype.hasOwnProperty.call(UNITS, u);
  function baseOf(qty, unit) { const u = UNITS[unit]; return { dim: u.dim, amount: qty * u.f }; }
  function gramsPer(dim, key) { return dim === 'mass' ? 1 : dim === 'vol' ? DENSITY[key] : PIECE_G[key]; }
  function factor(from, to, key) {           // how many `to` base units in one `from` base unit (null = unknown)
    if (from === to) return 1;
    const a = gramsPer(from, key), b = gramsPer(to, key);
    return (a == null || b == null) ? null : a / b;
  }
  const fmt = n => { const r = Math.round(n * 100) / 100; return String(r); };
  const fmtQty = (q, u) => fmt(q) + ' ' + u;

  // ---- Dates
  function dayNumber(iso) { const [y, m, d] = iso.split('-').map(Number); return Date.UTC(y, m - 1, d) / 86400000; }
  function daysUntil(expiry, today) { return expiry ? Math.round(dayNumber(expiry) - dayNumber(today)) : null; }
  function status(expiry, today, soonDays) {
    const d = daysUntil(expiry, today);
    if (d == null) return 'none';
    if (d < 0) return 'expired';
    return d <= soonDays ? 'soon' : 'fresh';
  }

  // ---- The strict rule
  function usable(item, o) { return !(o.excludeExpired && status(item.expiry, o.today, o.soonDays) === 'expired'); }

  function evaluate(recipe, pantry, o) {
    const groups = new Map(); // key -> {mass, vol, count, items[]}
    for (const it of pantry) {
      if (!usable(it, o) || !isUnit(it.unit)) continue;
      const key = canon(it.name), b = baseOf(it.qty, it.unit);
      if (!groups.has(key)) groups.set(key, { mass: 0, vol: 0, count: 0, items: [] });
      const g = groups.get(key); g[b.dim] += b.amount; g.items.push(it);
    }
    const lines = []; const expiringUsed = new Set();
    for (const ing of recipe.ingredients) {
      const key = canon(ing.name), need = baseOf(ing.qty, ing.unit), g = groups.get(key);
      const line = { name: ing.name, key, need: fmtQty(ing.qty, ing.unit), have: '', ok: false, reason: 'missing', detail: '' };
      if (g) {
        let total = g[need.dim], unitMismatch = false;
        for (const dim of ['mass', 'vol', 'count']) {
          if (dim === need.dim || g[dim] === 0) continue;
          const f = factor(dim, need.dim, key);
          if (f == null) unitMismatch = true; else total += g[dim] * f;
        }
        line.have = g.items.map(i => fmtQty(i.qty, i.unit)).join(' + ');
        if (total + 1e-9 >= need.amount) { line.ok = true; line.reason = 'ok'; line.detail = 'Have enough'; }
        else if (total === 0 && unitMismatch) { line.reason = 'unit'; line.detail = 'Units cannot be compared'; }
        else { line.reason = 'short'; line.detail = 'Not enough'; }
        line.total = total; line.needBase = need.amount;
        if (line.ok) g.items.forEach(i => { if (status(i.expiry, o.today, o.soonDays) === 'soon') expiringUsed.add(i.name); });
      } else { line.detail = 'Not in pantry'; }
      lines.push(line);
    }
    const missingCount = lines.filter(l => !l.ok).length;
    return { recipeId: recipe.id, ready: missingCount === 0, missingCount, lines, expiringUsed: [...expiringUsed] };
  }

  // Deduct a recipe from the pantry (earliest-expiring first). Returns {id: newQty}; newQty <= 0 means delete.
  function planCooking(recipe, pantry, o) {
    const next = new Map(pantry.map(i => [i.id, i.qty]));
    const order = i => (i.expiry ? dayNumber(i.expiry) : Infinity);
    for (const ing of recipe.ingredients) {
      const key = canon(ing.name), need = baseOf(ing.qty, ing.unit);
      let remaining = need.amount;
      const cands = pantry.filter(i => isUnit(i.unit) && usable(i, o) && canon(i.name) === key)
        .sort((a, b) => order(a) - order(b) || a.id - b.id);
      for (const it of cands) {
        if (remaining <= 1e-9) break;
        const u = UNITS[it.unit], f = factor(u.dim, need.dim, key);
        if (f == null) continue;
        const itemBase = next.get(it.id) * u.f, avail = itemBase * f, take = Math.min(remaining, avail);
        next.set(it.id, Math.round(((itemBase - take / f) / u.f) * 1000) / 1000);
        remaining -= take;
      }
    }
    const out = {};
    for (const i of pantry) { const n = next.get(i.id); if (n !== i.qty) out[i.id] = n < 0.005 ? 0 : n; }
    return out;
  }

  return { UNITS, canon, isUnit, evaluate, planCooking, daysUntil, status, fmt, fmtQty };
})();
if (typeof module !== 'undefined') module.exports = Engine;
