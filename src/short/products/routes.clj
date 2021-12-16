(ns short.products.routes
  (:require [short.products.controllers :as co]
            [short.middlewares :as mi]))

(defn routes [environment]
  (let [database (:database environment)]
    ["/products"
     {:middleware [[mi/wrap-jwt-auth environment] [mi/auth-middleware]]}
     [""
      {:post {:handler (co/create-product-controller! database)
              :parameters {:body {:sku string?
                                  :active boolean?
                                  :slug string?
                                  :price number?
                                  :quantity number?}}}}]]))
