ip="192.168.0.150"

echo 'Start building..'
cd C:/Users/shadow/IdeaProjects/Marta/spring-backend && ./gradlew build
echo 'Copy spring files..'
scp -i ~/.ssh/test \
  build/libs/spring-backend-0.0.1-SNAPSHOT.jar \
  ghost@$ip:/home/ghost/

#cd C:/Users/shadow/IdeaProjects/Marta/angular-frontend && ng build
#echo 'Copy angular files..'
#scp -i ~/.ssh/test -r dist/* ghost@$ip:/home/ghost/

echo 'Restart server..'

ssh -i ~/.ssh/test ghost@$ip << EOF
pgrep java | xargs kill -9
if [ -f /home/ghost/spring-backend-0.0.1-SNAPSHOT.jar ]; then
    nohup java -jar /home/ghost/spring-backend-0.0.1-SNAPSHOT.jar > /home/ghost/log.txt
else
    echo "JAR file not found."
fi
EOF
echo 'Started! Good luck)'
