# 5–7 minute video plan (matches PDF section 5.1)

Record at 1080p with your voice narrating throughout. Use the Android app for the live demo (the web preview
behaves identically and can be used to rehearse). Suggested timings:

| Time | Segment | What to show / say |
|---|---|---|
| 0:00–1:00 | **GitHub walkthrough** | Open your repo, scroll the commit history, explain how the project grew (data layer → screens → matching → polish). |
| 1:00–1:20 | App start | Launch the app. Explain the concept in one sentence. Settings ▸ **Load demo pantry**. |
| 1:20–2:10 | **Create + Read** | Pantry list (search “tomato”, filter *Expiring soon*, sort). Tap **Add**: save empty (validation errors), enter “Vegetable oil”, 500, ml, save. |
| 2:10–2:50 | **Update + Delete** | Edit *Tomatoes* 3 → 4. Cook tab: **Tomato pasta** now appears. Delete *Cheddar cheese*: cheese recipes disappear from Cook. Undo. |
| 2:50–3:30 | **Prove the strict rule** | Open *Egg fried rice* under **Almost there** (needs vegetable oil; if you added it, it is in Ready). Remove/re-add one ingredient and show a recipe leave/enter the list. Open a recipe ▸ *How this was checked*. |
| 3:30–3:50 | **Persistence** | Close the app fully, reopen: data is still there. Empty-pantry state: *No recipes match your pantry yet – add more ingredients*. |
| 3:50–6:20 | **Concept explanation** (pick 3, point at code) | ① Strict matching: `RecipeMatcher.evaluate` (why every line is checked; normalisation; unit conversion). ② RecyclerView + Adapter: `PantryAdapter.onBindViewHolder`. ③ Intents: `PantryListActivity.onEdit` → `AddEditIngredientActivity.EXTRA_ITEM_ID`. (Alternatives: Activity lifecycle → `onResume()` reloads; Room end-to-end → entity → DAO → `AppDatabase`.) |
| 6:20–7:00 | **Database justification** | Why SQLite/Room: local, offline, no server, typed DAOs, relations, matches the module. Why not Firebase/PostgreSQL. |

Tips: rehearse with the web preview’s **demo guide**; keep under 7:00 (the video is cut off at the limit); export MP4 (H.264) and
check the final ZIP is under 50 MB.

**Say it in your own words.** The assignment requires you to explain the code yourself, so read the classes you plan to discuss first.
