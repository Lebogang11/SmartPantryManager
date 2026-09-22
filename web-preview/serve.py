"""Optional: serve the preview on http://localhost:8080 (index.html also works by double-clicking it)."""
import http.server, socketserver, pathlib, os
os.chdir(pathlib.Path(__file__).parent)
with socketserver.TCPServer(("", 8080), http.server.SimpleHTTPRequestHandler) as s:
    print("Smart Pantry Manager preview: http://localhost:8080  (Ctrl+C to stop)")
    s.serve_forever()
