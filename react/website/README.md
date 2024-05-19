This is just me learning react and whatever tricks I can do with it. I am relying on "hash routing" - which has a slightly bad reputation - because I didn't want to get bogged down in next.js and such as well - nothing against next.js, just trying to focus on React first.

## Fixing errors:

1. I had to run
    npm update typescript
in order to make typescript errors go away.

2. Also to make another error dissapear
    npm install @babel/plugin-proposal-private-property-in-object --save-dev


## Running it
First install a gazillion node things, which will take forever and eat 370+MB (egads...) of disk space:

    npm install

Start up a development server on port 3000:

    npm run start

Build static site and test it out with a quickie python server:

    npm run build
    ./runpythonwebserver.sh