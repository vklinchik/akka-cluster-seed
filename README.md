akka-cluster-seed
=========================

A minimal seed template for an Akka cluster

See cluster documentation
http://doc.akka.io/docs/akka/2.5.3/scala/index-network.html

**Modules:**

`client - Client Application`

`commons - Shared library`

`master - Master node`

`worker - Worker node`

`seed - Cluster seed node`


_**Steps to start cluster**_

**1. Start seed node(s)**

`runMain akkaseed.seed.SeedApp 2551` #add additional nodes at ports 2552 and 2553



**To run master:**

`runMain akkaseed.master.MasterApp 2551`

`runMain akkaseed.master.MasterApp 2552`


**Run worker**

`runMain akkaseed.worker.WorkerApp 2551`

`runMain akkaseed.worker.WorkerApp 2552`



**Configuring seed nodes with JVM property system as follows:**

`-Dakka.cluster.seed-nodes.0=akka.tcp://ClusterSystem@127.0.0.1:2551`

`-Dakka.cluster.seed-nodes.1=akka.tcp://ClusterSystem@127.0.0.1:2552`
