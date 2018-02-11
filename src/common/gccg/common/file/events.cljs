(ns gccg.common.file.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [gccg.common.file.fx]))

(reg-event-fx
  :file/save
  (fn [cofx [_ filename data-path]]
    {:db           (assoc-in (:db cofx) [:common/file :filename] filename)
     :file.fx/save {:filename      filename
                    :data          (get-in (:db cofx) data-path)
                    :success-event [:file/save-success]
                    :error-event   [:file/save-error]}}))

(reg-event-fx
  :file/open
  (fn [cofx [_ opts]]
    {:file.fx/open opts}))
