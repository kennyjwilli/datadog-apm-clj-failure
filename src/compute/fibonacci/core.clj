(ns compute.fibonacci.core
  (:require
    [ring.adapter.jetty :as server]
    [hiccup.core :as hiccup]))

(defn app-html
  []
  (hiccup/html
    [:h1 "hello world"]))

(defn handler
  [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (app-html)})

(def app handler)

(defonce server (atom nil))

(defn -main
  [& args]
  (let [port (or (try (Integer/parseInt (first args)) (catch Exception _ nil)) 8080)]
    (println (str "Starting server on port " port "..."))
    (server/run-jetty app {:port port})))