(ns short.products.handlers
  (:require [short.products.logic :as l]
            [short.products.contracts :as c]
            [short.products.db :as db]
            [short.shared :as shared]
            [short.products.schemas :as s]))

(defn check-product-existence-by-slug!
  {:malli/schema [:=> [:cat c/ProductData :any] s/ProductQueryResult]}
  [product-input db]
  (db/get-product-by-slug! (:slug product-input) db))

(defn create-product!
  {:malli/schema [:=> [:cat c/ProductData :any] c/ProductOut]}
  [new-product db]
  (let [id (shared/generate-uuid!)
        created_at (shared/get-current-inst!)
        price (bigdec (:price new-product))]
    (-> new-product
        (assoc :price price)
        (l/product-creation id created_at)
        (db/create-product! db)
        shared/tempid->eid
        (db/get-product! db)
        l/internal->external)))

(defn get-product-by-slug!
  {:malli/schema [:=> [:cat string? :any] c/ProductOut]}
  [product-slug db]
  (-> product-slug
      (db/get-product-by-slug! db)
      flatten
      first
      l/internal->external))
