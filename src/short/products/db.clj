(ns short.products.db
  (:require [datahike.api :as d]
            [short.products.schemas :as s]
            [short.shared :as shared]))

(defn create-product!
  {:malli/schema [:=> [:cat s/Product :any] shared/Transaction]}
  [product db]
  (d/transact db [(assoc product :db/id -1)]))

(defn get-product!
  {:malli/schema [:=> [:cat int? :any] s/ExistingProduct]}
  [eid db]
  (d/pull @db '[*] eid))

(defn get-product-by-uuid!
  {:malli/schema [:=> [:cat uuid? :any] s/ProductQueryResult]}
  [uuid db]
  (d/q '[:find (pull ?e [*])
         :in $ ?uuid
         :where [?e :product/uuid ?uuid]]
       @db uuid))

(defn get-product-by-slug!
  {:malli/schema [:=> [:cat string? :any] s/ProductQueryResult]}
  [slug db]
  (d/q '[:find (pull ?e [*])
         :in $ ?slug
         :where [?e :product/slug ?slug]]
       @db slug))

(defn list-products!
  {:malli/schema [:=> [:cat :any] s/ProductQueryResult]}
  [db]
  (d/q '[:find (pull ?e [* {:product/variant [*]}])
         :in $
         :where [?e :product/uuid _]]
       @db))
