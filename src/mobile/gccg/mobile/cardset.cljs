(ns gccg.mobile.cardset
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [gccg.common.card :as card]
            [gccg.common.utils :as utils]
            [gccg.mobile.images :refer [cached-image]]
            [clojure.string :as s]))

(def ReactNative (js/require "react-native"))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def flat-list (r/adapt-react-class (.-FlatList ReactNative)))

(defn token->img [images]
  (let [img-map (reduce #(assoc %1 (:tag %2) (:file %2)) {} images)]
    (fn [[tag v]]
      (when-let [file (img-map tag)]
        [cached-image {:file     (str "graphics/Mtg/" file)
                       :alt-text v}]))))

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
                                [view {:key            name
                                       :flex-direction "row"}
                                 [cached-image {:file     (str "graphics/" (:dir game) "/" (:dir cardset) "/" (:graphics card))
                                                :alt-text name
                                                :style    {:flex 0
                                                           :width  100
                                                           :height 142}}]
                                 [view {:flex 1
                                        :flex-direction "column"
                                        :style {:padding-horizontal 10}}
                                  [view {:flex-direction "row"
                                         :flex-wrap "wrap"}
                                   [text {:style {:font-weight "bold"
                                                  :font-size   20}} name " "]
                                   (into [view {:flex-direction "row"
                                                :style {:padding-vertical 5}}]
                                         (-> (utils/tokenize token-fn (card/attr attr "cost"))
                                             (str->text)))]
                                  [text (card/type attr)]
                                  (into [view {:flex-direction "column"
                                               :flex-wrap "wrap"}]
                                        (map (fn [line]
                                               (into [view {:flex-direction "row"}]
                                                     (-> (utils/tokenize token-fn line)
                                                         (str->text))))
                                             (s/split-lines (:text card))))
                                  [text {:style {:font-weight "bold"}} (card/stats attr)]]])))]
          [view {:style {:flex-direction    "column"
                         :margin-top        80
                         :margin-horizontal 10
                         :align-items       "center"}}
           [flat-list {:data        (->> cardset :cards first :card (sort-by :name) (into-array))
                       :render-item render-card
                       :style       {:width "100%"}}]])))))
