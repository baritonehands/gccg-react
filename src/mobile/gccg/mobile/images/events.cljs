(ns gccg.mobile.images.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [clojure.string :as s]))

(reg-event-fx
  :images/init-from-game
  (fn [cofx [_ game]]
    {:db         (assoc (:db cofx) :images {})
     :dispatch-n (mapv #(vector :images/add-to-cache (str "graphics/" (:dir game) "/" (:file %)))
                       (:image game))}))

(reg-event-fx
  :images/add-to-cache
  (fn [cofx [_ file]]
    {:file.fx/open {:filename      file
                    :type          :image
                    :options       {:encoding "base64"}
                    :success-event [:images/add-to-cache-success file]}}))

(def ext->mime-type {".png"  "image/png"
                     ".jpg"  "image/jpeg"
                     ".jpeg" "image/jpeg"
                     ".gif"  "image/gif"})

(reg-event-db
  :images/add-to-cache-success
  (fn [db [_ file data]]
    (println "Loaded file" file)
    (let [ext-idx (s/last-index-of file ".")]
      (assoc-in db [:images file]
                (str "data:" (ext->mime-type (.substring file ext-idx)) ";base64," data)))))
