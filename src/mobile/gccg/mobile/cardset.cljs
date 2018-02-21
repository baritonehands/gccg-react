(ns gccg.mobile.cardset
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [gccg.common.card :as card]
            [gccg.common.utils :as utils]
            [clojure.string :as s]))

(def ReactNative (js/require "react-native"))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def flat-list (r/adapt-react-class (.-FlatList ReactNative)))

(defn token->img [images]
  (let [img-map (reduce #(assoc %1 (:tag %2) (:file %2)) {} images)]
    (println "Image map" img-map)
    (fn [[tag v]]
      (if-let [file (img-map tag)]
        [image {:source {:uri (str "graphics/Mtg/" (s/replace-all file #"/" ""))}
                :alt    v}]))))

(defn str->text [xs]
  (map (fn [v]
         (if (string? v)
           [text v]
           v)) xs))

(defn cardset [props]
  (let [game (subscribe [:game])]
    (fn [props]
      (if-let [cardset (-> @game :selected-set)]
        (let [render-card (fn [data]
                            (let [game (-> @game :meta)
                                  card (.-item data)
                                  {:keys [name attr]} card
                                  token-fn (token->img (:image game))]
                              (r/as-element
                                [view {:flex-direction "row"}
                                 [image {:source {:uri (str "graphics/" (:dir game) "/" (:dir cardset) "/" (:graphics card))}}]
                                 [view {:flex-direction "column"}
                                  (into [view {:flex-direction "row"} [text name]]
                                        (-> (utils/tokenize token-fn (card/attr attr "cost"))
                                            (str->text)))
                                  [text (card/type attr)]
                                  (into [view {:flex-direction "column"}]
                                        (map (fn [line]
                                               (into [view {:flex-direction "row"}]
                                                     (-> (utils/tokenize token-fn line)
                                                         (str->text))))
                                             (s/split-lines (:text card))))
                                  [text (card/stats attr)]]])))]
          [view {:style {:flex-direction "column" :margin-top 80 :margin-horizontal 10 :align-items "center"}}
           [flat-list {:data        (->> cardset :cards first :card (sort-by :name) (into-array))
                       :render-item render-card
                       :style       {:width "100%"}}]])))))
