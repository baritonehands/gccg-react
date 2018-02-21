(ns gccg.common.file.fx
  (:require [re-frame.core :refer [dispatch reg-fx]]
            [gccg.platform.xml.wrapper :refer [parse-xml]]
            [gccg.platform.file.wrapper :as file]
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
  (fn [{:keys [filename options type success-event error-event]}]
    (let [handler (fn [err data]
                    (if err
                      (dispatch (conj error-event err))
                      (dispatch
                        (conj success-event
                              (condp = type
                                :xml (-> (parse-xml data)
                                         (xml/entity->map))
                                data)))))]
      (if (not (empty? options))
        (file/read filename options handler)
        (file/read filename handler)))))
