## Reactivity web-front

## Dev
### Install Dependencies


#### /!\ some dep need ssh key on github (windows shell can't open passphrase prompt !) /!\
```sh
npm install
```

### This apps need a server to init WS so plz launch springboot application first

### Launch server with panel
```sh
npm start
```

### Unit test
```sh
npm test
```

### e2e

Once:
```sh
npm run webdriver:update
```

in a new cmd / bash
```sh
npm run webdriver:start
```

launch e2e test
```sh
npm run e2e
```
