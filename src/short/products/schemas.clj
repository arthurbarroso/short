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
   {:db/ident :product/variant
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many}])

(def Product
  [:map
   [:product/sku string?]
   [:product/active boolean?]
   [:product/slug string?]
   [:product/title string?]
   [:product/price number?]
   ;; [:product/quantity number?]
   [:product/uuid uuid?]
   [:product/created_at inst?]])

(def ExistingProduct
  [:map
   [:product/sku string?]
   [:product/active boolean?]
   [:product/slug string?]
   [:product/title string?]
   [:product/price number?]
   ;; [:product/quantity number?]
   [:product/uuid uuid?]
   [:product/created_at inst?]
   [:db/id int?]])

(def ProductQueryResult
  [:or
   [:vector
    [:vector
     ExistingProduct]]
   [:vector empty?]])

(def ProductUpdate
  [:map
   [:product/sku {:optional true} string?]
   [:product/active {:optional true} boolean?]
   [:product/slug {:optional true} string?]
   [:product/title {:optional true} string?]
   [:product/price {:optional true} number?]])
