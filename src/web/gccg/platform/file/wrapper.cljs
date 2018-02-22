(ns gccg.platform.file.wrapper
  (:require [ajax.core :refer [GET]]
            [clojure.string :as s]))

(defn remove-ext [filename]
  (let [idx (s/last-index-of filename ".")]
    (.substring filename 0 idx)))

(defn read
  ([filename cb]
   (read filename nil cb))
  ([filename _ cb]
   (let [segments (-> (s/split filename #"/")
                      (doto println))
         url (if (= (last (butlast segments)) "xml")
               (str "/api/games/" (remove-ext (last segments)))
               (str "/api/games/" (last (butlast segments)) "/cardsets/" (remove-ext (last segments))))]
     (GET url {:handler (fn [data] (cb nil data))
               :error-handler (fn [err] (cb err nil))}))))

(defn write [filename data cb])
