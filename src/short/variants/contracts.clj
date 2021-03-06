(ns short.variants.contracts)

(def VariantData
  [:map
   [:active boolean?]
   [:quantity number?]
   [:type string?]
   [:image-url string?]])

(def VariantOut
  [:maybe
   [:map
    [:variant/uuid uuid?]
    [:variant/active boolean?]
    [:variant/quantity number?]
    [:variant/type string?]
    [:variant/image-url string?]
    [:variant/created_at inst?]
    [:variant/product :map]]])

(def VariantUpdateData
  [:map
   [:active {:optional true} boolean?]
   [:quantity {:optional true} number?]
   [:type {:optional true} string?]
   [:image-url {:optional true} string?]])
