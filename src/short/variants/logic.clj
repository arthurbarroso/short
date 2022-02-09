(ns short.variants.logic
  (:require [short.variants.schemas :as s]
            [short.variants.contracts :as c]
            [short.products.schemas :as ps]))

(defn variant-creation
  {:malli/schema [:=> [:cat c/VariantData ps/ExistingProduct uuid? inst?] s/Variant]}
  [{:keys [active quantity type image-url]} product id created_at]
  {:variant/quantity quantity
   :variant/active active
   :variant/type type
   :variant/image-url image-url
   :variant/uuid id
   :variant/created_at created_at
   :variant/product product})

(defn internal->external
  {:malli/schema [:=> [:cat s/ExistingVariant] c/VariantOut]}
  [variant]
  (dissoc variant
          :db/id))

(defn prepend-variant-keyword [key]
  {:malli/schema [:=> [:cat :keyword] :keyword]}
  (keyword (str "variant/" (name key))))

(defn external->internal
  {:malli/schema [:=> [:cat c/VariantUpdateData] s/VariantUpdate]}
  [variant-data]
  (->> variant-data
       (map (fn [[k v]]
              (let [variant-keyword (prepend-variant-keyword k)]
                {variant-keyword v})))
       (into {})))
