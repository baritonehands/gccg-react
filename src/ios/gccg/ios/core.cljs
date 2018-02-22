(ns gccg.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [gccg.mobile.game :refer [game]]
            [gccg.mobile.cardset :refer [cardset]]
            [gccg.common.events]
            [gccg.common.subs]
            [gccg.mobile.images.events]
            [gccg.mobile.images.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def navigator-ios (r/adapt-react-class (.-NavigatorIOS ReactNative)))


(defn game-wrapper [{:keys [navigator]}]
  [game {:on-item-selected #(.push navigator #js {:component (r/reactify-component cardset)})}])

(defn app-root []
  (let [nav (r/atom nil)]
    (fn []
      [navigator-ios {:ref           (partial reset! nav)
                      :initial-route #js {:component (r/reactify-component game-wrapper)
                                          :title     "GCCG"}
                      :style {:flex 1}}])))

(defn init []
  (dispatch-sync [:initialize {:game-init-success [:images/init-from-game]}])
  (.registerComponent app-registry "GCCG" #(r/reactify-component app-root)))
