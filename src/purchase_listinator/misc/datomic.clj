(ns purchase-listinator.misc.datomic)


#_(defn datomic->entity [entities]
  (walk/prewalk dissoc-db-key entities))