export $(grep -v '^#' .env | xargs)
mkdir data
export DATASOURCE_URL="jdbc:sqlite:./data/visualiser.db"
export FRONT_PATH="http://localhost:3000"
./gradlew -x test build
./gradlew bootRun
