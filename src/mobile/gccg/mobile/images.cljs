(ns gccg.mobile.images
  (:require [re-frame.core :refer [subscribe]]
            [reagent.core :as r]))

(def ReactNative (js/require "react-native"))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))

(def default-style {:width  15
                    :height 15})

(defn cached-image [{:keys [file]}]
  (let [image-data (subscribe [:image file])]
    (fn [{:keys [style alt-text]}]
      (if @image-data
        [image {:source {:uri @image-data}
                :style  (merge default-style (or style {}))}]
        [view {:style (merge default-style
                             {:border-width 1
                              :border-color "#333333"}
                             (or style {}))}
         [text alt-text]]))))
