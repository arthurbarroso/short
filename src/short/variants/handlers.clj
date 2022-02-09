(ns short.variants.handlers
  (:require [short.variants.logic :as l]
            [short.shared :as shared]
            [short.variants.db :as db]
            [short.products.handlers :as ph]
            [short.variants.contracts :as c]
            [short.variants.schemas :as s]))

(defn create-variant!
  {:malli/schema [:=> [:cat c/VariantData :any] c/VariantOut]}
  [new-variant db]
  (let [id (shared/generate-uuid!)
        created_at (shared/get-current-inst!)
        product (ph/get-product-by-uuid!
                 (:product-id new-variant)
                 db)]
    (-> new-variant
        (l/variant-creation product id created_at)
        (db/create-variant! (:product-id new-variant) db)
        shared/tempid->eid
        (db/get-variant! db)
        l/internal->external)))

(defn update-variant!
  {:malli/schema [:=> [:cat c/VariantUpdateData uuid? :any] c/VariantOut]}
  [variant-data variant-uuid db]
  (-> variant-data
      l/external->internal
      (db/update-variant! variant-uuid db))
  (-> variant-uuid
      (db/get-variant-by-uuid! db)
      flatten
      first
      l/internal->external))

(defn check-variant-existence-by-uuid!
  {:malli/schema [:=> [:cat uuid? :any] s/VariantQueryResult]}
  [variant-uuid db]
  (db/get-variant-by-uuid! variant-uuid db))
