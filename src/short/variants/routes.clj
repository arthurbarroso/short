(ns short.variants.routes
  (:require [short.variants.controllers :as co]
            [short.middlewares :as mi]))

(defn routes [environment]
  (let [database (:database environment)]
    ["/variants"
     ["/:product-id"
      {:middleware [[mi/wrap-jwt-auth environment] [mi/auth-middleware]]
       :post {:handler (co/create-variant-controller! database)
              :parameters {:path {:product-id string?}
                           :body {:active boolean?
                                  :quantity number?
                                  :type string?
                                  :image-url string?}}
              :swagger {:security [{:bearer []}]}}}]]))
