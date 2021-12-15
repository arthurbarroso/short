(ns short.products.contracts)

(def ProductData
  [:map
   [:sku string?]
   [:active boolean?]
   [:slug string?]
   [:price number?]
   [:quantity number?]])

(def ProductOut
  [:maybe
   [:map
    [:product/sku string?]
    [:product/active boolean?]
    [:product/slug string?]
    [:product/price number?]
    [:product/quantity number?]
    [:product/uuid uuid?]
    [:product/created_at inst?]]])
