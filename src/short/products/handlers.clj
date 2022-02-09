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

(defn check-product-existence-by-uuid!
  {:malli/schema [:=> [:cat uuid? :any] s/ProductQueryResult]}
  [product-uuid db]
  (db/get-product-by-uuid! product-uuid db))

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

(defn get-product-by-uuid!
  {:malli/schema [:=> [:cat uuid? :any] c/ProductOut]}
  [product-uuid db]
  (-> product-uuid
      (db/get-product-by-uuid! db)
      flatten
      first
      l/internal->external))

(defn list-products!
  {:malli/schema [:=> [:cat :any] c/ProductListOut]}
  [db]
  (->> db
       db/list-products!
       flatten
       (map l/internal->external)))

(defn update-product!
  {:malli/schema [:=> [:cat c/ProductUpdateData uuid? :any] c/ProductOut]}
  [product-data product-uuid db]
  (-> product-data
      l/external->internal
      (db/update-product! product-uuid db))
  (-> product-uuid
      (db/get-product-by-uuid! db)
      flatten
      first
      l/internal->external))
