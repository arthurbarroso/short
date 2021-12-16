(ns short.products.controllers
  (:require [short.products.handlers :as h]
            [ring.util.response :as rr]))

(defn create-product-controller! [database]
  (fn [request]
    (let [product-input (-> request :parameters :body)
          existing-user? (h/check-product-existence-by-slug!
                          product-input
                          database)]
      (if (empty? existing-user?)
        (rr/created ""
                    (h/create-product!
                     product-input
                     database))
        (rr/bad-request {:error "Something went wrong"})))))
