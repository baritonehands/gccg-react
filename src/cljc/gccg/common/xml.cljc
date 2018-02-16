(ns gccg.common.xml)

(defn filter-content [entity key]
  (filter #(= key (:tag %)) (:content entity)))

(declare entity->map)

(defn merge-content [m content]
  (if (map? (first content))
    (assoc m (-> content first :tag) (mapv entity->map content))
    m))

(defn entity->map [entity]
  (let [keys (distinct (map :tag (:content entity)))]
    (reduce #(merge-content %1 (filter-content entity %2))
            (or (:attributes entity) (:attrs entity))
            keys)))
