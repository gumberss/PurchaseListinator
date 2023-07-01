(ns purchase-listinator.wires.purchase-list.out.db.share
  (:require [schema.core :as s]
            [purchase-listinator.misc.schema :as misc.schema]))

(def share-list-skeleton
  {:purchase-list-share/list-id     s/Uuid
   :purchase-list-share/customer-id s/Uuid
   :purchase-list-share/id          s/Uuid})
(misc.schema/loose-schema ShareList share-list-skeleton)


