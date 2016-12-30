#!/bin/bash
export BASE=~/dev
cd $BASE/temp || exit 1
cp $BASE/techknow/java/CrawlDBMetaData.java . || exit 1

javac CrawlDBMetaData.java && \
  java \
    -classpath ".:./postgres.jar" \
    CrawlDBMetaData \
    -user myuser \
    -pass mypass \
    -driver org.postgresql.Driver \
    -url jdbc:postgresql://192.168.99.100/postgres