const E = require('../../web-preview/src/engine.js'); const R = require('../../web-preview/data/recipes.json'); const D = require('../../web-preview/data/demo_pantry.json');
let pass = 0, fail = 0; const t = (n, c) => { c ? pass++ : (fail++, console.log('FAIL', n)); };
const today = '2026-09-20';
const add = (d, n) => { const x = new Date(Date.UTC(2026, 8, 20 + n)); return x.toISOString().slice(0, 10); };
const o = { excludeExpired: true, today, soonDays: 3 };
// normalisation
t('tomatoes', E.canon('Tomatoes') === 'tomato'); t('potatoes', E.canon('potatoes') === 'potato');
t('eggs', E.canon('  EGGS ') === 'egg'); t('berries', E.canon('berries') === E.canon('berry'));
t('fresh adj', E.canon('Fresh large tomatoes') === 'tomato'); t('spaghetti', E.canon('Spaghetti') === 'pasta');
t('capsicum', E.canon('capsicum') === 'bell pepper'); t('yogurt', E.canon('Greek Yogurt') === 'yoghurt');
t('evoo', E.canon('extra virgin olive oil') === 'olive oil'); t('loaves', E.canon('loaves') === E.canon('loaf'));
t('cookie', E.canon('cookie') === E.canon('cookies')); t('hummus', E.canon('hummus') === 'hummus');
t('chilli flakes', E.canon('Chilli flakes') === E.canon('chili flake')); t('parens', E.canon('Tomatoes (cherry)') === 'tomato');
const pantry = D.map((d, i) => ({ id: i + 1, name: d.name, qty: d.qty, unit: d.unit, expiry: d.offset == null ? null : add(today, d.offset) }));
const res = R.map(r => E.evaluate(r, pantry, o));
const ready = res.filter(r => r.ready).map(r => r.recipeId), almost = res.filter(r => r.missingCount === 1).map(r => r.recipeId);
console.log('READY', ready.join(','), '| ALMOST', almost.join(','));
t('ready set', JSON.stringify(ready) === JSON.stringify([1, 2, 5, 6, 8, 11, 13]));
t('almost set', JSON.stringify(almost) === JSON.stringify([3, 4, 15]));
// strictness: 5 needed, 4 present -> not suggested
const only4 = pantry.filter(p => p.name !== 'Cheddar cheese'); const r4 = R.map(r => E.evaluate(r, only4, o));
t('remove cheddar drops 1,11,13', [1, 11, 13].every(id => !r4[id - 1].ready) && r4[1].ready);
// quantity: exactly enough passes, slightly less fails
const chick = R[4]; const mk = q => [{id:1,name:'chicken breast',qty:q,unit:'g',expiry:null},{id:2,name:'rice',qty:200,unit:'g',expiry:null},{id:3,name:'onion',qty:1,unit:'pcs',expiry:null},{id:4,name:'garlic',qty:2,unit:'pcs',expiry:null},{id:5,name:'soy sauce',qty:2,unit:'tbsp',expiry:null}];
t('exact qty ok', E.evaluate(chick, mk(300), o).ready); t('299 g fails', !E.evaluate(chick, mk(299), o).ready);
t('0.3 kg ok', E.evaluate(chick, mk(300).map((x, i) => i === 0 ? { ...x, qty: 0.3, unit: 'kg' } : x), o).ready);
// unit differences: soy sauce in ml/l/cup vs tbsp
const soy = u => mk(300).map((x, i) => i === 4 ? { ...x, qty: u[0], unit: u[1] } : x);
t('30 ml = 2 tbsp', E.evaluate(chick, soy([30, 'ml']), o).ready); t('29 ml fails', !E.evaluate(chick, soy([29, 'ml']), o).ready);
t('0.25 cup ok', E.evaluate(chick, soy([0.25, 'cup']), o).ready); t('1 l ok', E.evaluate(chick, soy([1, 'l']), o).ready);
// split batches are summed
const split = mk(300).map(x => x); split[0] = { ...split[0], qty: 150 }; split.push({ id: 9, name: 'Chicken', qty: 150, unit: 'g', expiry: null });
t('batches sum + alias chicken', E.evaluate(chick, split, o).ready);
// singular/plural in pantry vs recipe
const omel = R[0]; const p2 = [{id:1,name:'Egg',qty:3,unit:'pcs',expiry:null},{id:2,name:'Tomatoes',qty:1,unit:'pcs',expiry:null},{id:3,name:'Cheddar',qty:30,unit:'g',expiry:null},{id:4,name:'Butter',qty:10,unit:'g',expiry:null}];
t('plural/alias omelette', E.evaluate(omel, p2, o).ready);
// count vs mass: 500 g tomato covers 4 pcs (480 g)
const tp = R[2]; const p3 = [{id:1,name:'pasta',qty:200,unit:'g'},{id:2,name:'tomatoes',qty:500,unit:'g'},{id:3,name:'onion',qty:1,unit:'pcs'},{id:4,name:'garlic',qty:2,unit:'pcs'},{id:5,name:'olive oil',qty:2,unit:'tbsp'},{id:6,name:'salt',qty:1,unit:'tsp'}];
t('mass covers count', E.evaluate(tp, p3, o).ready);
// unknown conversion -> unit mismatch, not a crash
const p4 = p3.map(x => x.name === 'onion' ? { ...x, qty: 1, unit: 'cup' } : x);
const e4 = E.evaluate(tp, p4, o); t('mismatch handled', !e4.ready && e4.lines.find(l => l.key === 'onion').reason === 'short' || e4.lines.find(l => l.key === 'onion').reason === 'unit');
// expired excluded / included
const expired = pantry.map(p => p.name === 'Cheddar cheese' ? { ...p, expiry: add(today, -1) } : p);
t('expired excluded', !E.evaluate(R[10], expired, o).ready); t('expired allowed when off', E.evaluate(R[10], expired, { ...o, excludeExpired: false }).ready);
// empty pantry
t('empty pantry -> none ready', R.every(r => !E.evaluate(r, [], o).ready));
// zero-qty / bad unit ignored safely
t('bad unit safe', !E.evaluate(omel, [{id:1,name:'egg',qty:3,unit:'xx'}], o).ready);
// cooking deducts FEFO and deletes empty
const plan = E.planCooking(R[0], pantry, o); const eggs = pantry.find(p => p.name === 'Eggs'); 
t('cook: eggs 6->3', plan[eggs.id] === 3); t('cook: butter 250->240', plan[pantry.find(p => p.name === 'Butter').id] === 240);
t('cook: tomato 3->2', plan[pantry.find(p => p.name === 'Tomatoes').id] === 2);
const plan2 = E.planCooking(R[4], mk(300), o); t('cook: chicken deleted', plan2[1] === 0);
// FEFO across two batches
const fe = [{id:1,name:'egg',qty:2,unit:'pcs',expiry:'2026-09-25'},{id:2,name:'egg',qty:4,unit:'pcs',expiry:'2026-09-21'}];
const pf = E.planCooking({ingredients:[{name:'eggs',qty:3,unit:'pcs'}]}, fe, o); t('FEFO uses soonest first', pf[2] === 1 && pf[1] === undefined);
// cooking then re-evaluating: recipe no longer ready when pantry exhausted
const after = pantry.map(p => plan[p.id] === undefined ? p : { ...p, qty: plan[p.id] }).filter(p => p.qty > 0);
t('after cooking omelette still ready (plenty)', E.evaluate(R[0], after, o).ready);
console.log(pass + ' passed, ' + fail + ' failed'); process.exit(fail ? 1 : 0);
