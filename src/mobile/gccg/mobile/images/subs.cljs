(ns gccg.mobile.images.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :image
  (fn [db [_ file]]
    (get-in db [:images file])))
