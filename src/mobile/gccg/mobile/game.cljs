(ns gccg.mobile.game
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe dispatch]]
            [gccg.mobile.cardset :refer [cardset]]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def flat-list (r/adapt-react-class (.-FlatList ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(defn game [props]
  (let [game (subscribe [:game])
        render-cardset (fn [data]
                         (r/as-element
                           [touchable-highlight {:on-press (fn []
                                                             (dispatch [:game/select-set (.-index data)])
                                                             ((:on-item-selected props)))
                                                 :underlay-color "#CCCCCC"}
                            [view {:style {:padding 10
                                           :border-bottom-color "#333333"
                                           :border-bottom-width 1}}
                             [text {:key (.-index data)} (-> data .-item .-source)]]]))]
    (fn [props]
      (println "Game props" props)
      [view {:style {:flex-direction "column"
                     :align-items "center"}}
       [flat-list {:data        (->> @game :meta :cardset clj->js)
                   :render-item render-cardset
                   :style {:width "100%"}}]])))
