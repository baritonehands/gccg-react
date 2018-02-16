(ns gccg.desktop.core
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [subscribe dispatch-sync]]
            [gccg.desktop.root :refer [root]]
            [gccg.common.subs]
            [gccg.common.events]))

(defonce reload-cnt (atom 0))

(defn root-component []
  @reload-cnt ; Seems to force a reload of nested components
  [root])

(defn init []
  (dispatch-sync [:initialize])
  (reagent/render
    [root-component]
    (js/document.getElementById "app-container")))
