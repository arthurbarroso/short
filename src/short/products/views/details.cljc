(ns short.products.views.details
  (:require [rum.core :as rum]
            [short.ui.template :as template]))

(rum/defc product-details [product]
  (template/template
   [:div {:class "product-page"}
    [:section {:class "product-data"}
     [:h1 (:product/title product)]]]))

(defn ^:export render [product]
  #?(:clj
     (rum/render-html (product-details product))
     :cljs
     (rum/hydrate (product-details product) js/document.body)))

;; (def b-product {:title "teste"})
;; (defn ^:export render-browser []
;;   #?(:clj ()
;;      :cljs (rum/hydrate (product-details b-product) (.getElementById js/document "root"))))

;; (comment
;;   (spit "store/teste.html" (render b-product)))
