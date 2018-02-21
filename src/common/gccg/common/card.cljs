(ns gccg.common.card
  (:require [clojure.string :as s]))

(defn token->img [images]
  (let [img-map (reduce #(assoc %1 (:tag %2) (:file %2)) {} images)]
    (fn [[tag v]]
      (if-let [file (img-map tag)]
        [:img.token {:src (str "graphics/Mtg/" (s/replace-all file #"/" ""))
                     :alt v}]
        [:span ""]))))

(defn attr [attrs key]
  (-> (filter #(= (:key %) key) attrs)
      first
      :value))

(defn type [attrs]
  (if-let [subtype (attr attrs "subtype")]
    (str (attr attrs "type") " - " subtype)
    (attr attrs "type")))

(defn stats [attrs]
  (if-let [power (attr attrs "power")]
    (str power " / " (attr attrs "toughness"))))
