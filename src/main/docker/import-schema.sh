docker-compose -f ./src/main/docker/docker-compose.yml -p demo-app exec -T db sh -c 'exec mysql -uroot -p"root"' < ./src/main/docker/schema.sql
