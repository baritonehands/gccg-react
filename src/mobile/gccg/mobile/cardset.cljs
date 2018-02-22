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
                                [view {:key   name
                                       :style {:flex-direction     "row"
                                               :border-bottom-width 1
                                               :border-bottom-color "#333333"
                                               :padding-horizontal 10
                                               :padding-vertical     10}}
                                 [cached-image {:file     (str "graphics/" (:dir game) "/" (:dir cardset) "/" (:graphics card))
                                                :alt-text name
                                                :style    {:flex   0
                                                           :width  100
                                                           :height 142}}]
                                 [view {:style {:flex           1
                                                :flex-direction "column"
                                                :padding-left   10}}
                                  [view {:style {:flex-direction "row"
                                                 :flex-wrap      "wrap"}}
                                   [text {:style {:font-weight "bold"
                                                  :font-size   20
                                                  :line-height 24}} name " "]
                                   (into [view {:flex-direction "row"
                                                :style          {:padding-vertical 5}}]
                                         (-> (utils/tokenize token-fn (card/attr attr "cost"))
                                             (str->text)))]
                                  [text {:style {:font-weight "bold"
                                                 :font-size   16
                                                 :line-height 20}} (card/type attr)]
                                  (into [view {:style {:flex-direction "column"
                                                       :flex-wrap      "wrap"}}]
                                        (map (fn [line]
                                               (into [view {:flex-direction "row"}]
                                                     (-> (utils/tokenize token-fn line)
                                                         (str->text))))
                                             (s/split-lines (:text card))))
                                  [text {:style {:font-weight "bold"}} (card/stats attr)]]])))
              update-viewable-image (fn [info]
                                      (let [changed (-> info .-changed (js->clj :keywordize-keys true))]
                                        (println "Changed items" changed)
                                        (doseq [row changed]
                                          (dispatch [(if (:isViewable row)
                                                       :images/add-to-cache
                                                       :images/remove-from-cache)
                                                     (str "graphics/Mtg/" (:dir cardset) "/" (-> row :item :graphics))]))))]
          [view {:style {:flex-direction "column"
                         :align-items    "center"}}
           [flat-list {:data                      (->> cardset :cards first :card (sort-by :name) (into-array))
                       :render-item               render-card
                       :style                     {:width "100%"}
                       :viewability-config        {:viewAreaCoveragePercentThreshold 0}
                       :on-viewable-items-changed update-viewable-image}]])))))
