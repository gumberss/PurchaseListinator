(ns purchase-listinator.misc.general)

(defn namespace-keys
  [m n]
  (let [n (name n)]
    (reduce-kv (fn [acc k v]
                 (let [new-kw (if (and (keyword? k)
                                       (not (qualified-keyword? k)))
                                (keyword (str n) (name k))
                                k) ]
                   (assoc acc new-kw v)))
               {} m)))

(defn unnamespace-keys
  [m]
  (reduce-kv (fn [acc k v]
               (let [new-kw (if (and (keyword? k)
                                     (qualified-keyword? k))
                              (keyword (name k))
                              k)]
                 (assoc acc new-kw v)))
             {} m))