(ns short.variants.handlers
  (:require [short.variants.logic :as l]
            [short.shared :as shared]
            [short.variants.db :as db]
            [short.products.handlers :as ph]
            [short.variants.contracts :as c]))

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
        (db/create-variant! db)
        shared/tempid->eid
        (db/get-variant! db)
        l/internal->external)))
