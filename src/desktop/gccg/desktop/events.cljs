(ns gccg.desktop.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [gccg.desktop.db :refer [app-db]]
            [gccg.common.file.events]
            [gccg.common.game.events]))

(reg-event-fx
  :initialize
  (fn [_ [_ name]]
    {:db app-db
     :dispatch-n [[:game/initialize "Mtg"]]}))
