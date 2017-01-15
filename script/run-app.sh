java -jar core/broadcaster/target/broadcaster-0.1.0-SNAPSHOT.jar&
echo $! > script/broadcaster.pid
cd front
npm start&
echo $! > ../script/front.pid
cd ..
sleep 10