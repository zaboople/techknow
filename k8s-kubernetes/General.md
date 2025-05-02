# Training Links
GitHub Repository for CKA: https://github.com/zealvora/certified-kubernetes-administrator
Discord Channel Link: https://kplabs.in/chat
Digital Ocean Credit Referral Code: https://m.do.co/c/74dcb0137794

# Glossary
Control Plane: Main thingie
Nodes can contain many pods.
Pods can contain more than one container, sharing network namespace and storage.


----------------------------------------
# Minikube
## Install, seems to come with kubectl
```
brew install minikube
```
## Start a minikube in colima

First we need to establish location of docker sock: Note that this will create a minikube profile under ~/.minikube/profiles/mykube
and later starts will just boot up with "minikube start"

```
export DOCKER_HOST=unix:///Users/tmotte/.colima/minikube/docker.sock
minikube start --nodes 2 --cpus 2 --memory 2048 --disk-size 2500m \
    --driver docker --container-runtime docker -p mykube
```

Note that I had a "--namespace mytest" but it seemed to break kubectl

Set minikube profile for commands after this:
```
minikube profile mykube
```

Minikube Cluster Network IP range is first 3 octets of the Minikube Cluster IP fetched, which is x.x.x.0/24:
```
minikube ip
```

To create another node in minikube (fun when you have daemonsets!):
```
minikube node add
```

Get a dashboard web site:
```
minikube dashboard
```

----------------------------------------
# Kubectl basic info:

Kubectl help is built in:

```
kubectl explain <thing>
kubectl explain pod
kubectl explain pod.spec
kubectl explain pod.spec.containers.startupProbe

kubectl api-resources
```

## Describe nodes in cluster:
```
kubectl get nodes
kubectl get nodes -o wide
kubectl describe nodes
kubectl describe node <node-name>
```

## Config for kubectl:
`less ~/.kube/config`

## Use special config file:
```
kubectl --kubeconfig <commands>
```
## Cluster info
```
kubectl cluster-info
kubectl cluster-info dump
```

# Resource Types:
  - Pod - Single instance of a running process (hmm it can have multiple containers...)
  - Deployment - Handles replicas of pods
  - Service - Exoses pods to network, load-balanced? (but not same as Ingress?)
  - ConfigMap - Non-secret configuration
  - Secret - Secrets storage
  - PersistentVolume - Storage
  - PersistentVolumeClaim - Request for storage
  - Ingress - Manages HTTP & HTTPS traffic coming in
  - Namespace

Uh-oh you have even more, and this is actually useful for working on yaml manifests,
because it lists necessary api versions, names for "kind", etc:
```
kubectl api-resources
```

# Pod & Container Basics

Run an nginx pod named "pod01":
```
kubectl run pod01 --image=nginx
```
Describe a pod:
```
kubectl describe pod <pod-name>
```
Delete a freakin pod:
```
kubectl delete pod <pod-name>
```
Generate YAML manifest instead of actually creating; you can specify client or server here, not sure diff:
```
kubectl run pod01 --image=nginx --dry-run=client -o yaml`
```

Get list of running pods:
```
kubectl get pods
kubectl get pods -o wide
kubectl describe pod <pod-name>
```
Get logs for our pod, or for a specific container in it:
```
kubectl logs <pod-name>
kubectl logs <pod-name> -c <container-name>
```
## Bash interaction / SSH

Run a bash command in our freakin pod:
```
kubectl exec -it <pod-name> -- bash -c 'ls; df'
```
Or run bash interactively - effectively an ssh into container:
```
kubectl exec -it <pod-name> -- bash
kubectl exec -it <pod-name> -c <container-name> -- bash
```
Another way but it creates a dead pod that tries to restart many times:
```
kubectl run stupid --image=busybox:latest --command -- ping "-c" "30" "google.com'
```

# YAML "manifest" Files:
Create arbitrary stuff (like a pod) with a yaml file:
```
kubectl apply -f myfile.yml
```
Delete it:
```
kubectl delete -f myfile.yml
```

Note that yaml files can have many objects delimited by a "---" on a line by itself between objects!

Primary things in a yaml:
```
    apiVersion: [ What it says, usually "v1"]
    kind: [Resource type, like "Pod"]
    metadata:
        name: [Resource name]
    spec:
        [all the config crud]
```
SPECIAL NOTE - Dash in yaml: The dash indicates the start of a member of a list of things. Here
we have foo as a list with two elements, each element being a map with two members:
```
    foo:
    - bar: swangle
      bash: dangle
    - bar: sworgle
      bash: doongle
```

Multi-pod containers require YAML - cannot be done with plain "kubectl run":
```
    spec:
      containers:
        - name: foo
        image: <image1>
        - name: bar
        image: <image2>
```

## Command & Arguments
To specify command and arguments to that command, add a couple things.
Note that "command" overrides ENTRYPOINT in dockerfile and "args" overrides "CMD"
(you could also just command & args as one "command" yaml list)
```
spec:
  containers:
    - name: foo
    image: <image1>
    command: ["/bin/echo"]
    args: ["hello", "world"]

    # Alternate form:
    command:
        - "/bin/echo"
        - "hello"
        - "world"
```
## Port exposing.
The port will be exposed no matter what, but we can make it
more explicit. Apparently there are ways to map to different port/IP in here
```
spec:
  containers:
    ports:
      - containerPort: 8080
```

# Labels
Get labels for running pods:
```
kubectl get pods --show-labels
```
This gets pod with label xxx and value yyy:
```
kubectl get pods -l xxx=yyy
```
Add labels:
```
kubectl label pod <podname> <labelname>=<value>
```
Yaml version, top level:
```
metadata:
  name: thingie
  labels:
    <labelname>: <value>
```

# Replicaset

This allows you to have a set number of copies of a pod, and can change the number of
instances on the fly. Dead pods are automatically restart by the replicaset.
Note: When you use "apply -f" to a running replicaset, it doesn't actually restart
or reconfigure pods; you have to kill pods off and then they will restart with new config.

You can only create them by using manifest. We have one: replicaset.example.yaml
```
kubectl apply -f replicaset.example.yaml
```

Get replicasets... second version is shorter
```
kubectl get replicaset --show-labels
kubectl get rs --show-labels
```

Try deleting a pod and it bounces back as new pod:
```
kubectl get pods --show-labels
kubectl delete pod -l myname=rs01
kubectl get pods --show-labels
```

Rescale the replicaset; notice how we have to put "rs/"
in front of the name or it won't work:
```
kubectl scale --replicas=2 rs/repset01
```

Delete replicaset:
```
kubectl delete replicaset repset01
```

Delete replicaset by label
```
kubectl delete replicaset -l myname=my-repset
```


# Deployment: A fancy upgrade to ReplicaSet
You can only create them by using manifest. We have one.
- Note that the manifest is about the same as replicaset;
  I just changed "kind: ReplicaSet" to "kind:Deployment" actually.
- If the underlying YAML changes enough to force redeploy,
  "apply -f" will kick off a rolling redeploy. Try changing
  "nginx" to "httpd" in manifest (or back). You ONLY get
  that redeploy if kube sees the right kind of changes to manifest,
  however.
- Deployments have underlying replicasets; they essentially
  *manage* those replicasets. If you delete a replicaset created
  by a deployment, kube does like it does with pods in an rs; it
  starts another replicaset up!


Create a deployment straight off of command line! You can use yaml
auto-generation technique to generate a manifest as well:
```
kubectl create deployment <name> --image=nginx
kubectl create deployment <name> --image=nginx --dry-run=client -o yaml
```

The more common way to create deployment, off of manifest:
```
kubectl apply -f deployment.example.yaml
```

Watch pods redeploy as needed:
```
kubectl apply -f deployment.example.yaml && kubectl get pods -w
```

This shows *previous* deploys as well as current:
```
kubectl get rs --show-labels
```

This only shows current:
```
kubectl get deployment --show-labels
```

To truly delete the deployment, do like so - don't delete the rs:
```
kubectl delete deployment <name>
```

Show history of deployments:
```
kubectl rollout history deployment/<deployment name>
kubectl rollout history deployment
```

Undo deployment! With `--to-revision`, you're using a number
from the rollout history:
```
kubectl rollout undo deployment
kubectl rollout undo deployment <name>
kubectl rollout undo deployment --to-revsion=<number>
```

You can scale deploys just like replicas:
```
kubectl scale --replicas=2 deploy/<name>
```

# NodeSelector - Label nodes for to pods to choose as destination

First you label your nodes:
```
kubectl label nodes <node-name> <label name>=<label value>
```

BTW, to remove label from node, use a "-" character (!!!):
```
kubectl label nodes <node-name> <label name>-
```

Add to the pod mainfest, goes parallel to the ":containers" element
Picks a label assigned to a node, so that your pod goes on that node
```
    containers:
        ....
    nodeSelector:
        <name>: <value>
```

# DaemonSet
Ensures that each NODE runs one pod for your daemonset config; when new nodes are created, a pod is
automatically deployed in it. Often used for things like log-collection daemons,
virus checkers, stats collection agents; things that you want on every pod as a
universal bare-minimum for pods.

Note that your daemonsset will appear as new pods, one per node. You can probably use
Node Selectors to actually pick out which node labels to look for.

Find out what daemonsets are running:
````
kubectl get daemonset
```

Remove a daemonset:
```
kubectl delete ds <name>
```

As manifest yaml goes, you really just do the same as Deployment, but with "kind:Daemonset"
(examples in this directory).

# Node Affinity

## Operators:
- `In`
- `NotIn`
- `Exists` - matches key only
- `DoesNotExist` - excudes nodes matching key
- `Gt` & `Lt` - works with numeric node values, < and >

## Preferences
`Required`: requiredDuringSchedulingIgnoredDuringExection
`Preferred`: - a softer requirement - preferredDuringSchedulingIgnoredDuringExection

## Example:
```
spec:
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: disktype
            operator: In
            values:
            - ssd
```

# Resources requests & limits
## Goes in like so:
      containers:
      - name: fluentd-elasticsearch
        image: quay.io/fluentd_elasticsearch/fluentd
        resources:
          requests:
            memory: "1Mi"
            cpu: "1"
          limits:
            memory: "2Mi"
            cpu: "2"
## How they work
requests is what we're asking for; limits are when we need to be killed

# --------------------------------
# Service
# --------------------------------
Can load-balance requests from pod A to pods B/C/D
Automatically keeps track of pod IP's
Can also expose pods to the outside world
## Four types
ClusterIP: Default, accessible only inside cluster
NodePort: Exposes service on a static port
LoadBalancer: Exposes service using cloud provider's load balancer
ExternalName: Maps service to external DNS name
## But that's misleading... I know about these
Service - Sort of gets ball rolling
Endpoint - More roll
# Get list of services:
kubectl get service -o wide
kubectl get svc -o wide
# This will show you an "Endpoints" that indicates the endpoints assigned:
kubectl describe service <name>

## NodePort
### First create a service like the contents of nodeport.yaml
kubectl apply -f nodeport.yaml
kubectl get services -o wide
### Now you can connect this up with minikube... via ssh tunnel? Note how
### "mynodeport" refers to service definition above.
### Refer to https://minikube.sigs.k8s.io/docs/handbook/accessing/
minikube service mynodeport --url
### Now find the tunnel in another terminal:
ps -ef | grep docker@127.0.0.1
### That will get you something like this, except with actual numbers for TUNNEL_PORT:CLUSTER_IP:TARGET_PORT
ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -N docker@127.0.0.1 -p 55972 -i /Users/FOO/.minikube/machines/minikube/id_rsa -L TUNNEL_PORT:CLUSTER_IP:TARGET_PORT
### Now you can go to TUNNEL_PORT via curl!
curl http://localhost:<TUNNEL_PORT>

## LoadBalancer
Creates a NodePort automatically; is one step up from such. This is a little
easier to work with than stupid nodeport.

### 1 We can use the same yaml as we did in our nodeport example,
###   and just change "type: NodePort" to "type:LoadBalancer":
### 2 Then run some minikube magic, ez pz
### 3 The service will be exposed on the port we picked in "port: 8081"
###   from our yaml
kubectl apply -f ./loadbalancer.yml
ssh tunnel
curl http://localhost:8081


# Ingress
This is like a LoadBalancer except that rather than creating a new LoadBalancer
every time you create a service, you can create one ingress for load-balancing
all your services at once.
You have two kinds:
    1. Ingress Controller
        Implements rules found in Ingress Resources
    2. Ingress Resource
- Use pathType: Prefix in most cases, because "Exact" matches only one thing
- Check ingress-controller.sh in this directory for usage

# Helm
- artifacthub.io has all the helmcharts
## Simple commands
helm list
helm repo list
## Add something
helm repo add <name> <url>
helm install <release> <chart>
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install my-nginx bitnami/nginx --version 19.1.1
### Search
helm repo search
helm search repo bitnami
helm search hub nginx
### Dump chart: Note that you can require a namespace with -n
helm template bitnami/nginx
helm template bitnami/nginx -n mynamespace
### Remove thing:
helm uninstall my-nginx

# Namespaces
You can usually append --namespace <name> to a command to specify a namespace
Or just use -n <name> for short!
## Built-in namespaces we know about are
default - For resources without a namespace
kube-system - For objects created by Kube
kube-public - A namespace for all users, usually for public data
kube-node-lease - Used for node heartbeat leases
## Set a default namespace
kubectl config set-context --current --namespace=<your-namespace-name>
## List all namespaces
kubectl get namespace
## Get pods for all namespaces:
kubectl get pods --all-namespaces -o wide
## Create
kubectl create namespace <name>
kubectl delete ns <name>

# Service accounts
kubectl get serviceaccounts --all-namespaces
kubectl get sa --all-namespaces
## There is a "default" service account that gets assigned to pods.
## Every namespace actually has its own "default" service account.
##
## Look at pod service account (pod name is derp here):
kubectl exec -it derp -- bash
root@derp:/# cd /var/run/secrets/kubernetes.io/serviceaccount/
root@derp:/var/run/secrets/kubernetes.io/serviceaccount# ls
ca.crt	namespace  token
### Note that the token file is used to talk to the dns & control plane servers
### inside servers, e.g.
    curl --insecure --header "Authorization: Bearer $token" https://....
### Remember that you can get those web server urls from
    kubectl cluster-info
