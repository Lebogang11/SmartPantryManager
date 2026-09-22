"""Rebuilds web-preview/index.html (one self-contained file) from src/ and data/.
Usage:  python3 build.py
"""
import pathlib
here = pathlib.Path(__file__).parent
template = (here / "src/template.html").read_text(encoding="utf-8")
engine = (here / "src/engine.js").read_text(encoding="utf-8").replace(
    "if (typeof module !== 'undefined') module.exports = Engine;", "")
recipes = (here / "data/recipes.json").read_text(encoding="utf-8")
demo = (here / "data/demo_pantry.json").read_text(encoding="utf-8")
out = template.replace("/*__ENGINE__*/", engine).replace("/*__RECIPES__*/", recipes).replace("/*__DEMO__*/", demo)
(here / "index.html").write_text(out, encoding="utf-8")
print(f"Built index.html ({len(out)//1024} KB)")
