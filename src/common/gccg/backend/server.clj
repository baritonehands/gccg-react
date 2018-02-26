(ns gccg.backend.server
  (:require [gccg.backend.handler :refer [app]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class)
  (:import (org.slf4j LoggerFactory)
           (ch.qos.logback.classic Level)))

(defn init-logger! []
  (let [logger (-> (LoggerFactory/getILoggerFactory)
                   (.getLogger "ROOT"))]
    (.setLevel logger Level/INFO)))

(defn -main [& args]
  (init-logger!)
  (let [port 3000]
    (run-jetty app {:port port :join? false})))
