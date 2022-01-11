(ns short.products.contracts)

(def ProductData
  [:map
   [:sku string?]
   [:active boolean?]
   [:slug string?]
   [:title string?]
   [:price number?]])
   ;; [:quantity number?]])

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
