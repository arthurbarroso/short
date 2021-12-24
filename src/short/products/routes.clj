(ns short.products.routes
  (:require [short.products.controllers :as co]
            [short.middlewares :as mi]
            [short.products.contracts :as c]))

(defn routes [environment]
  (let [database (:database environment)]
    ["/products"
     [""
      {:middleware [[mi/wrap-jwt-auth environment] [mi/auth-middleware]]
       :post {:handler (co/create-product-controller! database)
              :parameters {:body c/ProductData}
              :swagger {:security [{:bearer []}]}}
       :get {:handler (co/list-products-controller! database)
             :swagger {:security [{:bearer []}]}}}]
     ["/:product"
      {:get {:handler (co/render-product-by-slug-controller! database)
             :parameters {:path [:map [:product string?]]}}}]]))
