# React + Vite

This is just me learning react and whatever tricks I can do with it. I am relying on "hash routing" - which has a slightly bad reputation - because I didn't want to get bogged down in next.js and such as well - nothing against next.js, just trying to focus on React first.

In fact this is built using Vite, because Vite is the new Create-React-App I guess. Here, bang it out off of a template yourself:

    # "npm 7+, extra double-dash is needed:"
    npm create vite@latest vite-react -- --template react

## Running it

First install a gazillion node things:

    npm install

Start up a development server on port 8080 (configurable in package.json):

    npm run dev

## Getting ready for production:

Build static production site in `dist` directory and test it out with a quickie python server (not npm) on port 80:

    ./runpythonwebserver.sh

## Other build commands:

    npm run lint  # Run the linter and find out you made a mess
    npm run build # Built a production-ready deploy
