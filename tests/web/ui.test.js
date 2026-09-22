const { JSDOM } = require('jsdom'); const fs = require('fs');
const html = fs.readFileSync(process.argv[2] || '../../web-preview/index.html', 'utf8');
let pass = 0, fail = 0; const t = (n, c) => { c ? pass++ : (fail++, console.log('FAIL', n)); };
const sleep = ms => new Promise(r => setTimeout(r, ms));
(async () => {
  const errors = [];
  const dom = new JSDOM(html, { runScripts: 'dangerously', url: 'https://example.test/', pretendToBeVisual: true,
    beforeParse(w) { w.addEventListener('error', e => errors.push(e.message)); w.matchMedia = () => ({ matches: false, addEventListener() {} }); } });
  const w = dom.window, d = w.document, $ = s => d.querySelector(s), $$ = s => [...d.querySelectorAll(s)];
  const click = el => el.dispatchEvent(new w.MouseEvent('click', { bubbles: true }));
  const type = (el, v) => { el.value = v; el.dispatchEvent(new w.Event('input', { bubbles: true })); };
  const act = a => click($(`[data-action="${a}"]`));
  await sleep(700);
  t('no script errors at boot', errors.length === 0);
  t('skeleton gone, 18 items', $$('.item').length === 18);
  t('guide stats ready=7', $('#gstats').textContent.includes('7') && $$('.stat b')[1].textContent === '7');
  // search + normalisation
  type($('#q'), 'tomato'); t('search tomato -> 1', $$('.item').length === 1);
  type($('#q'), 'zzz'); t('no results state', $('.empty h3').textContent === 'No ingredients found');
  type($('#q'), ''); 
  act('filter:expired'); t('expired filter -> mushrooms', $$('.item').length === 1 && $('.item').textContent.includes('Mushrooms'));
  act('filter:all');
  // cook tab
  act('tab:suggested'); t('ready tab 7 cards', $$('.rcard').length === 7);
  t('no 1-missing recipe in ready list', !$('#screen').textContent.includes('Tomato pasta'));
  act('cook-tab:almost'); t('almost 3 cards separate', $$('.rcard').length === 3 && $('#screen').textContent.includes('kept out of your suggestions'));
  // validation: empty submit
  act('tab:pantry'); act('open-add'); $('#ing-form').dispatchEvent(new w.Event('submit', { bubbles: true, cancelable: true }));
  t('empty name error', $('#e-name').textContent.includes('Enter the ingredient name'));
  t('empty qty error', $('#e-qty').textContent.includes('Enter a quantity'));
  type($('#f-name'), 'Tom4to'); type($('#f-qty'), '0'); $('#ing-form').dispatchEvent(new w.Event('submit', { bubbles: true, cancelable: true }));
  t('letters-only error', $('#e-name').textContent.includes('letters only')); t('qty>0 error', $('#e-qty').textContent.includes('greater than 0'));
  type($('#f-qty'), 'abc'); $('#ing-form').dispatchEvent(new w.Event('submit', { bubbles: true, cancelable: true })); t('nan error', $('#e-qty').textContent.includes('must be a number'));
  type($('#f-name'), '<img src=x onerror=alert(1)>'); $('#ing-form').dispatchEvent(new w.Event('submit', { bubbles: true, cancelable: true })); t('html rejected by validation', $('#e-name').textContent.includes('letters only'));
  // create valid
  type($('#f-name'), 'Vegetable oil'); type($('#f-qty'), '500'); $('#f-unit').value = 'ml'; $('#ing-form').dispatchEvent(new w.Event('submit', { bubbles: true, cancelable: true }));
  t('created -> list 19', $$('.item').length === 19 && $('#screen').textContent.includes('Vegetable oil'));
  act('tab:suggested'); t('vegetable oil unlocks Egg fried rice', $('#screen').textContent.includes('Egg fried rice') && $$('.rcard').length === 8);
  // persistence
  const saved = JSON.parse(w.localStorage.getItem('smartpantry.v1')); t('persisted 19 items', saved.pantry.length === 19);
  click($('[data-guide="restart"]')); await sleep(700); act('tab:pantry'); t('after restart still 19', $$('.item').length === 19);
  // update tomatoes 3->4
  const tom = saved.pantry.find(p => p.name === 'Tomatoes'); act(`edit:${tom.id}`); type($('#f-qty'), '4'); $('#ing-form').dispatchEvent(new w.Event('submit', { bubbles: true, cancelable: true }));
  act('tab:suggested'); t('update -> Tomato pasta appears', $('#screen').textContent.includes('Tomato pasta'));
  // delete cheddar w/ confirm + undo
  act('tab:pantry'); const ch = saved.pantry.find(p => p.name === 'Cheddar cheese'); act(`delete:${ch.id}`);
  t('confirm dialog shown', !!$('.dialog') && $('.dialog h2').textContent.includes('Cheddar')); act('dialog-ok');
  t('deleted -> 18 items', $$('.item').length === 18);
  act('tab:suggested'); t('cheese recipes vanish', !$('#screen').textContent.includes('Cheese toastie') && !$('#screen').textContent.includes('Cheese and tomato omelette'));
  act('tab:pantry'); act('toast-action'); t('undo restores', $$('.item').length === 19);
  act('tab:suggested'); t('cheese recipes back', $('#screen').textContent.includes('Cheese toastie'));
  // recipe detail + cook
  const card = $$('.rcard').find(c => c.textContent.includes('Cheese toastie')); click(card);
  t('detail shows method + trace', $('.method') && $('.trace summary').textContent.includes('How this was checked') && $$('.ings li').length === 3);
  act('cook'); t('cook dialog lists deductions', $$('.dialog li').length >= 3); act('dialog-ok');
  const after = JSON.parse(w.localStorage.getItem('smartpantry.v1')); t('bread 6->2', after.pantry.find(p => p.name === 'Bread').qty === 2);
  t('back on cook tab after cooking', $('.tabs') !== null);
  // almost -> detail -> add missing prefilled
  act('cook-tab:almost'); click($$('.rcard').find(c => c.textContent.includes('Spaghetti aglio')));
  t('detail shows missing', $('.status.almost') && $('.mark.no')); act('add-missing:3');
  t('prefilled chilli', $('#f-name').value.toLowerCase().includes('chilli') && $('#f-unit').value === 'tsp');
  $('#ing-form').dispatchEvent(new w.Event('submit', { bubbles: true, cancelable: true })); t('back to detail after save', !!$('.status.ok'));
  // settings
  act('back'); act('tab:settings'); act('toggle:alerts'); t('alerts toggled off', JSON.parse(w.localStorage.getItem('smartpantry.v1')).settings.alerts === false);
  $('#s-days').value = '7'; $('#s-days').dispatchEvent(new w.Event('change', { bubbles: true })); t('window 7 saved', JSON.parse(w.localStorage.getItem('smartpantry.v1')).settings.soonDays === 7);
  // empty state
  act('demo-clear'); act('dialog-ok'); act('tab:suggested'); t('zero-match message', $('#screen').textContent.includes('No recipes match your pantry yet'));
  act('tab:pantry'); t('empty pantry state', $('#screen').textContent.includes('Your pantry is empty'));
  act('demo-load'); act('dialog-ok'); t('demo reloaded 18', $$('.item').length === 18);
  t('no runtime errors overall', errors.length === 0); if (errors.length) console.log(errors);
  console.log(pass + ' passed, ' + fail + ' failed'); process.exit(fail ? 1 : 0);
})();
