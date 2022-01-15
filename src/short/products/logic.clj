(ns short.products.logic
  (:require [short.products.schemas :as s]
            [short.products.contracts :as c]
            [short.shared :as shared]))

(defn product-creation
  {:malli/schema [:=> [:cat c/ProductData uuid? inst?] s/Product]}
  [{:keys [sku title active slug price]} id created_at]
  {:product/sku sku
   :product/active active
   :product/slug (shared/slugify slug)
   :product/title title
   :product/price price
   :product/uuid id
   :product/created_at created_at})

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
