(ns gccg.common.xml)

(defn filter-content [entity key]
  (filter #(= key (:tag %)) (:content entity)))

(declare entity->map)

(defn merge-content [m content]
  (assoc m (-> content first :tag) (mapv entity->map content)))

(defn entity->map [entity]
  (let [keys (distinct (map :tag (:content entity)))]
    (reduce #(merge-content %1 (filter-content entity %2))
            (:attributes entity)
            keys)))
