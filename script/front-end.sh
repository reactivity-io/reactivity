#!/bin/bash

rm -rf ~/.nvm
git clone https://github.com/creationix/nvm.git ~/.nvm
cd ~/.nvm
git checkout `git describe --abbrev=0 --tags`
source ~/.nvm/nvm.sh
nvm install
cd $TRAVIS_BUILD_DIR/front
npm install

cd $TRAVIS_BUILD_DIR
java -jar core/broadcaster/target/broadcaster-0.1.0-SNAPSHOT.jar&
echo $! > script/broadcaster.pid
cd $TRAVIS_BUILD_DIR/front
npm start&
echo $! > ../script/front.pid
sleep 10

cd $TRAVIS_BUILD_DIR/front
npm run webdriver:update
npm run webdriver:start&
echo $! > ../script/webdriver.pid
sleep 5
xvfb-run npm test
xvfb-run npm run e2e

npm run dist
cd $TRAVIS_BUILD_DIR
