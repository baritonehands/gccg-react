(ns gccg.android.core
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
(def back-handler (.-BackHandler ReactNative))
(def view (r/adapt-react-class (.-View ReactNative)))
(def view-pager-android (r/adapt-react-class (.-ViewPagerAndroid ReactNative)))

(defn js-keys [obj]
  (-> js/Object (.keys obj)))

(defn app-root []
  (let [nav (r/atom nil)
        page (r/atom 0)
        update-page (fn [n]
                      (let [new-page (swap! page + n)]
                        (println "Updating page" new-page)
                        (if (>= new-page 0)
                          (.setPage @nav new-page)
                          (.exitApp back-handler))
                        true))
        handler (fn []
                  (println (js-keys @nav))
                  (update-page -1))]
    (r/create-class
      {:component-did-mount    #(.addEventListener back-handler "hardwareBackPress" handler)
       :component-will-unmount #(.removeEventListener back-handler "hardwareBackPress" handler)
       :reagent-render         (fn []
                                 [view-pager-android {:ref          (partial reset! nav)
                                                      :initial-page 0
                                                      :on-page-selected (fn [e]
                                                                          (reset! page (-> e .-nativeEvent .-position)))
                                                      :style        {:flex 1}}
                                  [view [game {:on-item-selected #(update-page 1)}]]
                                  [view [cardset {}]]])})))

(defn init []
  (dispatch-sync [:initialize {:game-init-success [:images/init-from-game]}])
  (.registerComponent app-registry "GCCG" #(r/reactify-component app-root)))
