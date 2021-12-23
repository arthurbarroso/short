(ns short.variants.routes
  (:require [short.variants.controllers :as co]
            [short.middlewares :as mi]
            [short.variants.contracts :as c]))

(defn routes [environment]
  (let [database (:database environment)]
    ["/variants"
     ["/:product-id"
      {:middleware [[mi/wrap-jwt-auth environment] [mi/auth-middleware]]
       :post {:handler (co/create-variant-controller! database)
              :parameters {:path [:map [:product-id string?]]
                           :body c/VariantData}
              :swagger {:security [{:bearer []}]}}}]]))
