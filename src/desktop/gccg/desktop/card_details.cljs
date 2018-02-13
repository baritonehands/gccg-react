(ns gccg.desktop.card-details
  (:require [clojure.string :as s]
            [gccg.common.utils :as utils]))

(defn token->img [images]
  (let [img-map (reduce #(assoc %1 (:tag %2) (:file %2)) {} images)]
    (fn [[tag v]]
      (if-let [file (img-map tag)]
        [:img.token {:src (str "graphics/Mtg/" (s/replace-all file #"/" ""))}]
        [:span ""]))))

(defn get-attr [attrs key]
  (-> (filter #(= (:key %) key) attrs)
      first
      :value))

(defn card-type [attrs]
  (if-let [subtype (get-attr attrs "subtype")]
    (str (get-attr attrs "type") " - " subtype)
    (get-attr attrs "type")))

(defn card-stats [attrs]
  (if-let [power (get-attr attrs "power")]
    (str power " / " (get-attr attrs "toughness"))))

(defn card-details [props]
  (let [{:keys [game cardset card]} props
        {:keys [name text attr]} card
        token-fn (token->img (:image game))]
    [:div.card
     [:img {:src (str "graphics/" (:dir game) "/" (:dir cardset) "/" (:graphics card))
            ;:width "100"
            ;:height "100"
            }]
     (into [:h3.title name "\u00A0\u00A0"] (utils/tokenize token-fn (get-attr attr "cost")))
     [:h4.type (card-type attr)]
     (into [:span.text]
           (mapcat (fn [line]
                     (conj (utils/tokenize token-fn line) [:br]))
                   (s/split-lines text)))
     [:div.stats (card-stats attr)]
     ;(for [[s idx] (map vector (s/split-lines text) (range))]
     ;  (into [:span {:key idx}] ))]

     ;[:div (str card)]
     [:div.clearfix]
     ]))
