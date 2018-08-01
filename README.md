# Dashboard for Office TVs

## run locally
gradle run

see results: http://localhost:8080/

## build Docker image
cp ./build/libs/office-tv-dashboard ./docker
docker build -t dashboard .

## run Docker imgae
docker run -p 8080:8080 dashboard

see results: http://localhost:8080/