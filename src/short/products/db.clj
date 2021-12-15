(ns short.products.db
  (:require [datahike.api :as d]
            [short.products.schemas :as s]))

(defn create-product!
  {:malli/schema [:=> [:cat s/Product :any] s/Product]}
  [product db]
  (d/transact db [(assoc product :db/id -1)]))

(defn get-product!
  {:malli/schema [:=> [:cat int? :any] s/Product]}
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
