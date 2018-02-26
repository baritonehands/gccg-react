(ns gccg.backend.api.games
  (:require [liberator.core :refer [defresource]]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(defn load-resource [filename]
  (log/info "Loading " filename)
  (-> (io/resource filename)
      slurp))

; Liberator is overkill for this, but I wanted to try it bidi
(defresource games-resource
             :available-media-types ["application/xml"]
             :allowed-methods [:get]
             :handle-ok (fn [ctx]
                          (load-resource (str "xml/" (get-in ctx [:request :route-params :name]) ".xml"))))

(defresource cardsets-resource
             :available-media-types ["application/xml"]
             :allowed-methods [:get]
             :handle-ok (fn [ctx]
                          (let [{:keys [name cardset]} (get-in ctx [:request :route-params])]
                            (load-resource (str "xml/" name "/" cardset ".xml")))))

(def routes [[["games/" :name] games-resource]
             [["games/" :name "/cardsets/" :cardset] cardsets-resource]])
