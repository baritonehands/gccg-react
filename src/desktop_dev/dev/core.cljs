(ns ^:figwheel-no-load dev.core
  (:require [figwheel.client :as fw :include-macros true]
            [re-frame.core :refer [clear-subscription-cache!]]
            [gccg.desktop.core :as core] :reload))

(enable-console-print!)

(defn force-reload! []
      (clear-subscription-cache!)
      (swap! core/reload-cnt inc))

(fw/start {
           :websocket-url   "ws://localhost:3449/figwheel-ws"
           :jsload-callback force-reload!})

(core/init)
