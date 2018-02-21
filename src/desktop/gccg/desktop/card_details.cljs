(ns gccg.desktop.card-details
  (:require [clojure.string :as s]
            [gccg.common.utils :as utils]
            [gccg.common.card :as card]))

(defn token->img [images]
  (let [img-map (reduce #(assoc %1 (:tag %2) (:file %2)) {} images)]
    (fn [[tag v]]
      (if-let [file (img-map tag)]
        [:img.token {:src (str "graphics/Mtg/" (s/replace-all file #"/" ""))
                     :alt v}]
        [:span ""]))))

(defn card-details [props]
  (let [{:keys [game cardset card]} props
        {:keys [name text attr]} card
        token-fn (token->img (:image game))]
    [:div.card
     [:img {:src (str "graphics/" (:dir game) "/" (:dir cardset) "/" (:graphics card))
            }]
     (into [:h3.title name "\u00A0\u00A0"] (utils/tokenize token-fn (card/attr attr "cost")))
     [:h4.type (card/type attr)]
     (into [:span.text]
           (mapcat (fn [line]
                     (conj (utils/tokenize token-fn line) [:br]))
                   (s/split-lines text)))
     [:div.stats (card/stats attr)]
     [:div.clearfix]
     ]))
