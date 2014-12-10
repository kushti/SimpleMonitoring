Simple Monitoring
=====================================

Simple Monitoring Written in Scala On the Top of Akka Framework.

Installation & Run
------------------

* If MongoDB is not installed, install it to a database machine:


    apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10

    echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list

    apt-get update

    apt-get install -y mongodb-org

* Install Java 7 & SBT

* Run the application from its folder with `sbt start`



Properties
----------

Properties of the application are located in the file `application.conf`

    # Mongo servers (Replica Set)
    server.names = [localhost]
    # Database name to get baskets from
    db.name=test

Put Data via HTTP API Example
-----------------------------

    curl -X POST -H "Content-type: application/json" -d '{"value":0,"basket":"basket","timestamp":1381436764000}' http://localhost:9000/json/
    curl -X POST -H "Content-type: application/json" -d '{"value":77,"basket":"basket","timestamp":1381436744000}' http://localhost:9000/json/
    curl -X POST -H "Content-type: application/json" -d '{"value":50,"basket":"basket","timestamp":1381436754000}' http://localhost:9000/json/
    curl -X POST -H "Content-type: application/json" -d '{"value":5,"basket":"basket","timestamp":1381436774000}' http://localhost:9000/json/
    curl -X POST -H "Content-type: application/json" -d '{"value":8,"basket":"basket","timestamp":1381436784000}' http://localhost:9000/json/
    curl -X POST -H "Content-type: application/json" -d '{"value":30,"basket":"basket","timestamp":1381436794000}' http://localhost:9000/json/

Getting Data via HTTP API Example
--------------------

    curl -X GET -H "Content-type: application/json" -d '{"basket":"basket","count":5}' http://localhost:9000/json/

