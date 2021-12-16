(ns short.products.routes
  (:require [short.products.controllers :as co]))

(defn routes [environment]
  (let [database (:database environment)]
    ["/products"
     [""
      {:post {:handler (co/create-product-controller! database)
              :parameters {:body {:sku string?
                                  :active boolean?
                                  :slug string?
                                  :price number?
                                  :quantity number?}}}}]]))
