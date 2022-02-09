(ns short.variants.db
  (:require [datahike.api :as d]
            [short.variants.schemas :as s]
            [short.shared :as shared]))

(defn create-variant!
  {:malli/schema [:=> [:cat s/Variant uuid? :any] shared/Transaction]}
  [variant product-uuid db]
  (d/transact db [(assoc variant :db/id -1)
                  {:db/id [:product/uuid product-uuid]
                   :product/variant -1}]))

(defn get-variant!
  {:malli/schema [:=> [:cat int? :any] s/ExistingVariant]}
  [eid db]
  (d/pull @db '[*] eid))

(defn get-variant-by-uuid!
  {:malli/schema [:=> [:cat uuid? :any] s/VariantQueryResult]}
  [uuid db]
  (d/q '[:find (pull ?e [* {:variant/product [:product/uuid :product/title]}])
         :in $ ?uuid
         :where [?e :variant/uuid ?uuid]]
       @db uuid))

(defn update-variant!
  {:malli/schema [:=> [:cat s/VariantUpdate uuid? :any] s/ExistingVariant]}
  [variant-data variant-uuid db]
  (d/transact db [(merge {:db/id [:variant/uuid variant-uuid]} variant-data)]))
