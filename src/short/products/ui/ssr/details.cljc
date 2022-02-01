(ns short.products.ui.ssr.details
  (:require [rum.core :as rum]
            [short.shared.ui.template :as template]
            [short.shared.ui.text :as text]
            [short.shared.ui.button :as button]
            #?(:cljs [short.shared.cookies :as cookies])))

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
       :sizing "text-sm"})
     (button/button {:on-click #()
                     :text "Buy"
                     :title "purchase"})]]))

;; {:keys [on-click text disabled title extra-style]}

(defn ^:export render [product]
  #?(:clj
     (rum/render-html (product-details product))
     :cljs
     (rum/hydrate (product-details product) (.getElementById js/document "root"))))

#?(:cljs (defn ^:export hydrate []
           (let [product (cookies/get-cookie! "product")]
             (render product))))
