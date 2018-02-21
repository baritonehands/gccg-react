(ns gccg.mobile.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [subscribe dispatch dispatch-sync]]
            [gccg.mobile.game :refer [game]]
            [gccg.common.events]
            [gccg.common.subs]
            [gccg.mobile.images.events]
            [gccg.mobile.images.subs]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def flat-list (r/adapt-react-class (.-FlatList ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def navigator-ios (r/adapt-react-class (.-NavigatorIOS ReactNative)))

(defn app-root []
  (let [nav (r/atom nil)]
    (fn []
      [navigator-ios {:ref           (partial reset! nav)
                      :initial-route #js {:component (r/reactify-component game)
                                          :title     "GCCG"}
                      :style {:flex 1}}])))

(defn init []
  (dispatch-sync [:initialize {:game-init-success [:images/init-from-game]}])
  (.registerComponent app-registry "GCCG" #(r/reactify-component app-root)))
