(ns short.products.views.details
  (:require [rum.core :as rum]
            [short.ui.template :as template]
            [short.ui.text :as text]
            #?(:cljs [short.cookies :as cookies])))

(rum/defc product-details [product]
  (template/template
   [:div {:class "product-page"}
    [:section {:class "product-heading"}
     (text/typography
      {:text (:product/title product)
       :variant "h2"})]
    [:section {:class "product-pricing"}
     (text/typography
      {:text (str "price: "
                  (:product/price product))
       :variant "h4"})]
    [:section {:class "product-stock"}
     (text/typography
      {:text (str "currently in-stock: "
                  (:product/quantity product))
       :variant "p"
       :sizing "text-sm"})]]))

(defn ^:export render [product]
  #?(:clj
     (rum/render-html (product-details product))
     :cljs
     (rum/hydrate (product-details product) (.getElementById js/document "root"))))

#?(:cljs (defn ^:export hydrate []
           (let [product (cookies/get-cookie! "product")]
             (render product))))
