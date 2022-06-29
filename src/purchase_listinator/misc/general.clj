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

; https://github.com/clojure-cookbook/clojure-cookbook/blob/master/01_primitive-data/1-24_uuids.asciidoc
(defn squuid []
  (let [uuid (java.util.UUID/randomUUID)
        time (System/currentTimeMillis)
        secs (quot time 1000)
        lsb (.getLeastSignificantBits uuid)
        msb (.getMostSignificantBits uuid)
        timed-msb (bit-or (bit-shift-left secs 32)
                          (bit-and 0x00000000ffffffff msb))]
    (java.util.UUID. timed-msb lsb)))