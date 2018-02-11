(ns gccg.desktop.root
  (:require [re-frame.core :refer [subscribe dispatch]]
            [gccg.desktop.list-view :refer [list-view]]
            [gccg.desktop.card-details :refer [card-details]]))

(defn root []
  (let [game @(subscribe [:game])]
    [:div.root
     [:div {:style {"width" "50%"}}
      [:div "Hello, " (-> game :meta :name) "!"]
      (into [list-view {:item-click #(dispatch [:game/select-set %])}]
            (for [{:keys [source]} (-> game :meta :cardset)]
              source))]
     [:div {:style {"width" "50%"}} ;(-> game :selected-set str)
      (if-let [cardset (-> game :selected-set)]
        (into [list-view {
                          ;:item-click #(dispatch [:canvas/add-child (get-item %)])
                          }]
              (for [card (-> cardset :cards first :card)]
                [card-details {:key  (:name card)
                               :dir  (str (-> game :meta :dir) "/" (-> cardset :dir))
                               :card card}])
              ))]]))
