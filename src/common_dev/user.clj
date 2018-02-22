(ns user
  (:use gccg.backend.handler
        [figwheel-sidecar.repl-api :as ra]
        [ring.middleware file-info file]
        ring.server.standalone
        bidi.schema)
  (:require [schema.core :as s]))
;; This namespace is loaded automatically by nREPL

;; read project.clj to get build configs
(def profiles (->> "project.clj"
                   slurp
                   read-string
                   (drop-while #(not= % :profiles))
                   (apply hash-map)
                   :profiles))

(def cljs-builds (get-in profiles [:dev :cljsbuild :builds]))

(defn start-figwheel
  "Start figwheel for one or more builds"
  [& build-ids]
  (ra/start-figwheel!
    {:build-ids  build-ids
     :all-builds cljs-builds
     :figwheel-options {:css-dirs ["electron_app/css" "resources/public/css"]
                        :ring-handler `app}})
  (ra/cljs-repl))

(defn stop-figwheel
  "Stops figwheel"
  []
  (ra/stop-figwheel!))

(defonce server (atom nil))

(defn get-handler []
  ;; #'app expands to (var app) so that when we reload our code,
  ;; the server is forced to re-resolve the symbol in the var
  ;; rather than having its own copy. When the root binding
  ;; changes, the server picks it up without having to restart.
  (-> #'app
      ; Makes static assets in $PROJECT_DIR/resources/public/ available.
      (wrap-file "resources")
      ; Content-Type, Content-Length, and Last Modified headers for files in body
      (wrap-file-info)))

(defn start-server
  "used for starting the server in development mode from REPL"
  [& [port]]
  (let [port (if port (Integer/parseInt port) 3000)]
    (reset! server
            (serve (get-handler)
                   {:port port
                    :auto-reload? true
                    :join? false
                    :open-browser? false}))
    (println (str "You can make API requests to http://localhost:" port))))

(defn stop-server []
  (.stop @server)
  (reset! server nil))