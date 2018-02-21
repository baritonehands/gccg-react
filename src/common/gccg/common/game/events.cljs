(ns gccg.common.game.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [gccg.common.game.db :refer [init-db]]))

(reg-event-fx
  :game/initialize
  (fn [cofx [_ {:keys [name success-event]}]]
    {:db           (assoc-in cofx [:db :game] init-db)
     :file.fx/open {:filename      (str "xml/" name ".xml")
                    :type          :xml
                    :success-event [:game/init-success {:success-event success-event}]
                    :error-event   [:game/init-error]}}))

(reg-event-fx
  :game/init-success
  (fn [cofx [_ {:keys [success-event]} data]]
    (let [res {:db (assoc-in (:db cofx) [:game :meta] data)}]
      (if success-event
        (assoc res :dispatch (conj success-event data))
        res))))

(reg-event-db
  :game/init-error
  (fn [db [_ err]]
    (assoc-in db [:game :error] err)))

(reg-event-fx
  :game/select-set
  (fn [cofx [_ idx]]
    (let [{:keys [dir cardset]} (get-in (:db cofx) [:game :meta])
          {:keys [source]} (get cardset idx)]
      {:file.fx/open {:filename      (str "xml/" dir "/" source)
                      :type          :xml
                      :success-event [:game/select-set-success]
                      :error-event   [:game/select-set-error]}})))

(reg-event-db
  :game/select-set-success
  (fn [db [_ data]]
    (assoc-in db [:game :selected-set] data)))

(reg-event-db
  :game/select-set-error
  (fn [db [_ err]]
    (assoc-in db [:game :selected-set] err)))
