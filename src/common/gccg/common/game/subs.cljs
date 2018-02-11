(ns gccg.common.game.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :game
  (fn [db _]
    (:game db)))
