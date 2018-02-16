(ns gccg.desktop.root
  (:require [re-frame.core :refer [subscribe dispatch]]
            [gccg.desktop.list-view :refer [list-view]]
            [gccg.desktop.card-details :refer [card-details]]))

(defn root []
  (let [game (subscribe [:game])]
    (fn []
      [:div.root
       [:div {:style {"width" "30%"}}
        (into [list-view {:item-click #(dispatch [:game/select-set %])}]
              (for [{:keys [source]} (->> @game :meta :cardset (sort-by :source))]
                source))]
       [:div {:style {"width" "70%"}}                       ;(-> game :selected-set str)
        (if-let [cardset (-> @game :selected-set)]
          (into [list-view {
                            ;:item-click #(dispatch [:canvas/add-child (get-item %)])
                            }]
                (for [card (->> cardset :cards first :card (sort-by :name))]
                  [card-details {:key     (:name card)
                                 :game    (-> @game :meta)
                                 :cardset cardset
                                 :card    card}])
                ))]])))
