(ns short.variants.routes
  (:require [short.variants.controllers :as co]
            [short.middlewares :as mi]
            [short.variants.contracts :as c]))

(defn routes [environment]
  (let [database (:database environment)]
    ["/variants"
     ["/:product-id"
      {:middleware [[mi/jws-middleware environment]]
       :post {:handler (co/create-variant-controller! database)
              :parameters {:path [:map [:product-id string?]]
                           :body c/VariantData}
              :swagger {:security [{:bearer []}]}}}]
     ["/update/:variant-uuid"
      {:put {:handler (co/update-variant-controller! database)
             :parameters {:path [:map [:variant-uuid string?]]
                          :body c/VariantUpdateData}}}]]))
