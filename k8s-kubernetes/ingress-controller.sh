function waitFor() {
    echo -e -n "\nPress enter: "; read nothing;
    echo "";
}
function describe() {
    kubectl get pods
    waitFor
    kubectl get service
    waitFor
    kubectl describe ingress main-ingress
    waitFor
    kubectl get pods -n ingress-nginx
    waitFor
    kubectl get service -n ingress-nginx
}
function startup() {
    echo "Make pods..."
    kubectl run pod-nginx --image=nginx
    kubectl run pod-httpd --image=httpd
    waitFor

    kubectl get pods
    waitFor

    kubectl expose pod pod-nginx --name service-nginx --port=80 --target-port=80
    kubectl expose pod pod-httpd --name service-httpd --port=80 --target-port=80
    kubectl get service
    waitFor

    kubectl create ingress main-ingress --class=nginx \
        --rule="nginx.internal/*=service-nginx:80" \
        --rule="httpd.internal/*=service-httpd:80"
    kubectl describe ingress main-ingress
    waitFor

    kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.12.0/deploy/static/provider/cloud/deploy.yaml
    waitFor

    kubectl get pods -n ingress-nginx
    kubectl get service -n ingress-nginx

    echo "
        MACINTOSH INSTRUCTIONS:
        Now you can expose either of service nginx or httpd
        Remember that this hangs terminal!

            minikube service service-nginx --url
            minikube service service-httpd --url

        Remember that this hangs terminal too:

            minikube tunnel

        Then you can do either of:

            curl http://127.0.0.1 --header "Host: httpd.internal"
            curl http://127.0.0.1 --header "Host: nginx.internal"

        Done.
    "
}

function shutdown() {
    echo ""
    echo "Expect a long delay as we delete the ingress!!!"
    echo ""
    kubectl delete -f \
        https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.12.0/deploy/static/provider/cloud/deploy.yaml
    kubectl delete ingress main-ingress
    kubectl delete service service-nginx
    kubectl delete service service-httpd
    kubectl delete pod pod-nginx
    kubectl delete pod pod-httpd
}

while [[ $1 ]]; do
    if [[ $1 == "start" ]]; then
        startup
    elif [[ $1 == "stop" ]]; then
        shutdown
    elif [[ $1 == "describe" ]]; then
        describe
    else
        echo "Dunno what to do with: $1"
    fi
    shift
done
