#!/usr/bin/env sh

GRADLE_CLEAN=true
GRADLE_INSTALL=true

clean_old_docker_artifacts() {

    docker stop rd-judicial-api
    docker stop rd-judicial-db

    docker rm rd-judicial-api
    docker rm rd-judicial-db

    docker rmi hmcts/rd-judicial-api
    docker rmi hmcts/rd-judicial-db

    docker volume rm rd-judicial-api_rd-judicial-db-volume
}

execute_script() {

  clean_old_docker_artifacts

   docker-compose down -v

   docker system prune –af

  ./gradlew clean assemble

  export SERVER_PORT="${SERVER_PORT:-8090}"

  pwd

  chmod +x bin/*

  docker-compose up
}

execute_script
