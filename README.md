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

`runMain mindriot.akkaseed.seed.SeedApp 2551` #add additional nodes at ports 2552 and 2553



**2. Run master node(s)**

`runMain mindriot.akkaseed.master.MasterApp 2651` #add additional node at ports 2652 if desired


**3. Run worker nodes(s)**

`runMain mindriot.akkaseed.worker.WorkerApp 2751` #add additional node at ports 2752 +


**4. Run ClientApp**

`runMain mindriot.akkaseed.client.ClientApp`


**Configuring seed nodes with JVM properties:**

`-Dakka.cluster.seed-nodes.0=akka.tcp://ClusterSystem@127.0.0.1:2551`

`-Dakka.cluster.seed-nodes.1=akka.tcp://ClusterSystem@127.0.0.1:2552`
