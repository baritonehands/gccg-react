(ns cljs.user
  (:require [gccg.common.file.wrapper :as file]
            [gccg.common.xml :as xml]
            [tubax.core :refer [xml->clj]]))

(defn load-xml [name]
  (let [res (atom nil)
        filename (str "../gccg-svn/core/xml/" name)]
    (println "Opening" filename)
    (file/read filename
               (fn [err data]
                 (if err
                   (println err)
                   (reset! res (-> data str xml->clj xml/entity->map)))))
    res))

(defn cardsets [xml]
  (let [{:keys [name dir cardset]} xml]
    (mapv (comp #(load-xml (str dir "/" %))
                :source)
          cardset)))
