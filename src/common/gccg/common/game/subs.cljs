(ns gccg.common.game.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :game/info
  (fn [db _]
    (-> db :game :meta)))

(reg-sub
  :game/cardset
  (fn [db _]
    (-> db :game :selected-set)))
