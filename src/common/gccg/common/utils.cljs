(ns gccg.common.utils
  (:require [clojure.string :as s]))

(def token-re (re-pattern #"\{([^\}]+)\}"))

(defn tokenize [token-fn s]
  (if-let [tokens (re-seq token-re (or s ""))]
    (loop [[tk v] (first tokens)
           rem (next tokens)
           res []
           last 0]
      (let [idx (s/index-of s tk last)
            next-res (conj res (.substring s last idx) (token-fn [tk v]))]
        (if rem
          (recur (first rem) (next rem) next-res (+ idx (count tk)))
          (->> (conj next-res (.substring s (+ idx (count tk))))
               (remove empty?)))))
    [s]))
