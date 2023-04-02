(ns purchase-listinator.misc.general
  (:require [schema.core :as s])
  (:import (java.util UUID)))

(defn namespace-keys
  [m n]
  (when (not= nil m)
    (let [n (name n)]
      (reduce-kv (fn [acc k v]
                   (let [new-kw (if (and (keyword? k)
                                         (not (qualified-keyword? k)))
                                  (keyword (str n) (name k))
                                  k)]
                     (assoc acc new-kw v)))
                 {} m))))

(defn unnamespace-keys
  [m]
  (when (not= nil m)
    (reduce-kv (fn [acc k v]
                 (let [new-kw (if (and (keyword? k)
                                       (qualified-keyword? k))
                                (keyword (name k))
                                k)]
                   (assoc acc new-kw v)))
               {} m)))

; https://github.com/clojure-cookbook/clojure-cookbook/blob/master/01_primitive-data/1-24_uuids.asciidoc
(s/defn squuid []
  (let [uuid (UUID/randomUUID)
        time (System/currentTimeMillis)
        secs (quot time 1000)
        lsb (.getLeastSignificantBits uuid)
        msb (.getMostSignificantBits uuid)
        timed-msb (bit-or (bit-shift-left secs 32)
                          (bit-and 0x00000000ffffffff msb))]
    (UUID. timed-msb lsb)))

(defn assoc-some
  ([map key val]
   (if val
     (assoc map key val)
     map))
  ([map key val & kvs]
   (let [ret (assoc-some map key val)]
     (if kvs
       (if (next kvs)
         (recur ret (first kvs) (second kvs) (nnext kvs))
         (throw (IllegalArgumentException.
                  "assoc expects even number of arguments after map/vector, found odd number")))
       ret))))

(defn dissoc-nils
  [map]
  (apply dissoc map (for [[k v] map :when (nil? v)] k)))

(defn dissoc-if
  [map prop cond]
  (if (cond (prop map))
    (dissoc map prop)
    map))
