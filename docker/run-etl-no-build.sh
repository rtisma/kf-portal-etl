#!/bin/bash

cd ..
sbt pipeline/assembly
cp kf-portal-etl-pipeline/target/scala-2.11/kf-portal-etl.jar ./docker/etl

cd docker/

# download spark
SPARK_VERSION=2.3.1
./smart-download-spark.sh $SPARK_VERSION

cp kf_etl.conf ./etl/
cp spark-${SPARK_VERSION}-bin-hadoop2.7.tgz ./etl/

docker-compose -f docker-compose-no-build.yml up -d

#clean
rm etl/kf-portal-etl.jar
rm etl/kf_etl.conf
rm etl/spark-${SPARK_VERSION}-bin-hadoop2.7.tgz
