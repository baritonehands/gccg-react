(ns gccg.desktop.list-view)

(defn list-view [props & children]
  (let [{:keys [item-hover item-click]
         :or   {item-hover (constantly nil)
                item-click (constantly nil)}} props]
    [:ul.list-view
     (for [[item idx] (map vector children (range))]
       [:li.list-item {:key            idx
                       :on-mouse-enter #(item-hover idx)
                       :on-mouse-leave #(item-hover -1)
                       :on-click       #(item-click idx)}
        item])]))
