(ns gccg.common.file.fx
  (:require [re-frame.core :refer [dispatch reg-fx]]
            [tubax.core :refer [xml->clj]]
            [gccg.common.file.wrapper :as file]
            [gccg.common.xml :as xml]))

(reg-fx
  :file.fx/save
  (fn [{:keys [filename data success-event error-event]}]
    (let [json (.stringify js/JSON (clj->js data))]
      (file/write filename json
                  (fn [err]
                    (if err
                      (dispatch (into error-event err))
                      (dispatch success-event)))))))

(reg-fx
  :file.fx/open
  (fn [{:keys [filename type success-event error-event]}]
    (file/read filename
               (fn [err data]
                 (if err
                   (dispatch (conj error-event err))
                   (let [sdata (str data)]
                     (dispatch
                       (conj success-event
                             (condp = type
                               :xml (-> (xml->clj sdata)
                                        (xml/entity->map))
                               sdata)))))))))
