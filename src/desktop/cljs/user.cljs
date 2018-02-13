(ns cljs.user
  (:require [gccg.common.file.wrapper :as file]
            [gccg.common.xml :as xml]
            [tubax.core :refer [xml->clj]]
            [clojure.data.xml :refer [parse-str]]
            [clojure.string :as s]))

(def init-times {xml->clj  []
                 parse-str []})
(defonce times (atom {xml->clj  []
                      parse-str []}))
(defn reset-times [] (reset! times init-times))

(declare load-xml)

(defn time-xml [data xml-fn]
  (let [res (atom nil)
        s (with-out-str
            (reset! res (time (xml-fn data))))
        ms (-> (s/split s #" ")
               (get 2)
               (#(.parseFloat js/window %)))]
    (swap! times update xml-fn conj ms)
    @res))

(defn avg [coll]
  (/ (reduce + coll) (count coll)))

(defn load-xml
  ([name] (load-xml parse-str name))
  ([xml-fn name]
   (let [res (atom nil)
         filename (str "../gccg-svn/core/xml/" name)
         start (system-time)]
     ;(println "Opening" filename)
     (file/read filename
                (fn [err data]
                  (if err
                    (println err)
                    (reset! res (-> data str (time-xml xml-fn) xml/entity->map
                                    )))))
     res)))

(defn cardsets [xml]
  (let [{:keys [name dir cardset]} xml]
    (mapv (comp #(load-xml (str dir "/" %))
                :source)
          cardset)))

(defn new-avg [avg n x]
  (-> (* avg n)
      (+ x)
      (/ (inc n))))
