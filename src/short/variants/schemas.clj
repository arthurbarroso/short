(ns short.variants.schemas)

(def variant-db-schema
  [;;variant/uuid
   {:db/ident :variant/uuid
    :db/valueType :db.type/uuid
    :db/unique :db.unique/identity
    :db/cardinality :db.cardinality/one}
   ;;variant/active
   {:db/ident :variant/active
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one}
   ;;variant/quantity
   {:db/ident :variant/quantity
    :db/valueType :db.type/bigint
    :db/cardinality :db.cardinality/one}
   ;;variant/type
   {:db/ident :variant/type
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   ;;variant/image-url
   {:db/ident :variant/image-url
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   ;;variant/created_at
   {:db/ident :variant/created_at
    :db/valueType :db.type/instant
    :db/cardinality :db.cardinality/one}
   ;;variant/product
   {:db/ident :variant/product
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}])

(def Variant
  [:map
   [:variant/uuid uuid?]
   [:variant/active boolean?]
   [:variant/quantity number?]
   [:variant/type string?]
   [:variant/image-url string?]
   [:variant/created_at inst?]
   [:variant/product :map]])

(def ExistingVariant
  [:map
   [:variant/uuid uuid?]
   [:variant/active boolean?]
   [:variant/quantity number?]
   [:variant/type string?]
   [:variant/image-url string?]
   [:variant/created_at inst?]
   [:variant/product :map]
   [:db/id int?]])

(def VariantUpdate
  [:map
   [:variant/active {:optional true} boolean?]
   [:variant/quantity {:optional true} number?]
   [:variant/type {:optional true} string?]
   [:variant/image-url {:optional true} string?]])

(def VariantQueryResult
  [:or
   [:vector
    [:vector
     ExistingVariant]]
   [:vector empty?]])
