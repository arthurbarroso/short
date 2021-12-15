(ns short.products.logic
  (:require [short.products.schemas :as s]
            [short.products.contracts :as c]
            [short.shared :as shared]))

(defn product-creation
  {:malli/schema [:=> [:cat c/ProductData uuid? inst?] s/Product]}
  [{:keys [sku active slug price quantity]} id created_at]
  {:product/sku sku
   :product/active active
   :product/slug slug
   :product/price price
   :product/quantity quantity
   :product/uuid id
   :product/created_at created_at})

(defn tx->id
  {:malli/schema [:=> [:cat s/ProductTx] int?]}
  [product-tx]
  (shared/tempid->eid product-tx))

(defn internal->external
  {:malli/schema [:=> [:cat s/ExistingProduct] c/ProductOut]}
  [product]
  (dissoc product
          :db/id))
