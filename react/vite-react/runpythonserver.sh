cd $(dirname $0)
npm run build || exit 1
cd dist

# Ref. https://docs.python.org/3/library/http.server.html
python -m http.server 80