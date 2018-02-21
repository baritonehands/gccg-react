(ns gccg.common.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [gccg.common.db :refer [app-db]]
            [gccg.common.file.events]
            [gccg.common.game.events]))

(reg-event-fx
  :initialize
  (fn [_ [_ {:keys [game-init-success]}]]
    {:db app-db
     :dispatch-n [[:game/initialize {:name "mtg"
                                     :success-event game-init-success}]]}))
