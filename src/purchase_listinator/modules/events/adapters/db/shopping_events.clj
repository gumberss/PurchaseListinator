(ns purchase-listinator.modules.events.adapters.db.shopping-events
  (:require
    [purchase-listinator.misc.datomic :as misc.datomic]
    [schema.core :as s]
    [purchase-listinator.modules.events.schemas.wires.out.db.shopping-events :as wires.out.db.shopping-events]
    [purchase-listinator.modules.events.schemas.models.shopping-events :as models.shopping-event]
    [purchase-listinator.misc.general :as misc.general]))

(s/defn internal->db :- wires.out.db.shopping-events/ShoppingEvent
  [{:keys [properties] :as event} :- models.shopping-event/ShoppingEvent]
  (-> (assoc event :properties (str properties))
      (misc.general/dissoc-nils)
      (misc.general/namespace-keys "shopping-event")))

(s/defn db->internal :- models.shopping-event/ShoppingEvent
  [{:shopping-event/keys [category-id item-id properties] :as event} :- wires.out.db.shopping-events/ShoppingEventDb]
  (-> (misc.general/unnamespace-keys event)
      (misc.datomic/datomic->entity)
      (assoc :properties (or (clojure.edn/read-string properties) {})
             :category-id category-id
             :item-id item-id)))