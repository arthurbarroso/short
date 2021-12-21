(ns short.products.logic
  (:require [short.products.schemas :as s]
            [short.products.contracts :as c]
            [short.shared :as shared]))

(defn product-creation
  {:malli/schema [:=> [:cat c/ProductData uuid? inst?] s/Product]}
  [{:keys [sku title active slug price quantity]} id created_at]
  {:product/sku sku
   :product/active active
   :product/slug slug
   :product/title title
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

(defn product-html->view
  {:malli/schema [:=> [:cat :string s/ExistingProduct] :map]}
  [product-html product]
  {:pre-rendered-html product-html
   :hydrate-script-fn "short$.products.views.details.hydrate()"
   :scripts-to-include "<script src=\"/assets/js/shared.js\"></script><script src=\"/assets/js/products.js\"></script>"
   :title (:product/title product)
   :stylesheets "<link rel=\"stylesheet\" href=\"/assets/stylesheet.css\">"})
