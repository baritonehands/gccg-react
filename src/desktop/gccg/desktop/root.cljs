(ns gccg.desktop.root
  (:require [re-frame.core :refer [subscribe dispatch]]
            [gccg.desktop.list-view :refer [list-view]]
            [gccg.desktop.card-details :refer [card-details]]))

(defn root []
  (let [game-sub (subscribe [:game/info])
        cardset-sub (subscribe [:game/cardset])]
    (fn []
      [:div.root
       [:div {:style {"width" "30%"}}
        (into [list-view {:item-click #(dispatch [:game/select-set %])}]
              (for [{:keys [source]} (->> @game-sub :cardset)]
                source))]
       [:div {:style {"width" "70%"}}
        (if-let [cardset @cardset-sub]
          (into [list-view {}]
                (for [card (->> cardset :cards first :card (sort-by :name))]
                  [card-details {:key     (:name card)
                                 :game    @game-sub
                                 :cardset cardset
                                 :card    card}])
                ))]])))
