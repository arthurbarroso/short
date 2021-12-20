(ns short.products.schemas)

(def Product
  [:map
   [:product/sku string?]
   [:product/active boolean?]
   [:product/slug string?]
   [:product/title string?]
   [:product/price number?]
   [:product/quantity number?]
   [:product/uuid uuid?]
   [:product/created_at inst?]])

(def ProductTx
  [:map
   [:db-before :any]
   [:db-after :any]
   [:tx-data :any]
   [:tempids :map]])

(def ExistingProduct
  [:map
   [:product/sku string?]
   [:product/active boolean?]
   [:product/slug string?]
   [:product/title string?]
   [:product/price number?]
   [:product/quantity number?]
   [:product/uuid uuid?]
   [:product/created_at inst?]
   [:db/id int?]])

(def ProductQueryResult
  [:or
   [:vector
    [:vector
     ExistingProduct]]
   [:vector empty?]])
