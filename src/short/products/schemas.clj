(ns short.products.schemas)

(def product-db-schema
  [;;product/uuid
   {:db/ident :product/uuid
    :db/valueType :db.type/uuid
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one}
   ;;product/title
   {:db/ident :product/title
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   ;;product/sku
   {:db/ident :product/sku
    :db/unique :db.unique/value
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   ;;product/active
   {:db/ident :product/active
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   ;;product/slug
   {:db/ident :product/slug
    :db/unique :db.unique/value
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   ;;product/price
   {:db/ident :product/price
    :db/valueType :db.type/bigdec
    :db/cardinality :db.cardinality/one}
   ;;product/created_at
   {:db/ident :product/created_at
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}
   ;;product/quantity
   {:db/ident :product/quantity
    :db/valueType :db.type/bigint
    :db/cardinality :db.cardinality/one}])

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
