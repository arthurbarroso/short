(ns short.products.contracts
  (:require [malli.core :as m]))

(def ProductData
  [:map
   [:sku string?]
   [:active boolean?]
   [:slug string?]
   [:title string?]
   [:price number?]])

(def ProductOut
  [:maybe
   [:map
    [:product/sku string?]
    [:product/active boolean?]
    [:product/slug string?]
    [:product/title string?]
    [:product/price number?]
    [:product/uuid uuid?]
    [:product/created_at inst?]
    [:product/variant {:optional true} [:vector :map]]]])

(def ProductListOut
  [:vector ProductOut])

(def ProductSku
  (m/schema [:string {:min 3}]))

(def ProductSlug
  (m/schema [:string {:min 3}]))

(def ProductTitle
  (m/schema [:string {:min 3}]))

(defn validate-title [title]
  (if title
    (m/validate ProductTitle title)
    true))

(defn validate-slug [slug]
  (if slug
    (m/validate ProductSlug slug)
    true))

(defn validate-sku [sku]
  (if sku
    (m/validate ProductSku sku)
    true))

(defn validate-form [form-data]
  (m/validate ProductData form-data))
