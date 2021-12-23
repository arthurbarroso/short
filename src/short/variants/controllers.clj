(ns short.variants.controllers
  (:require [short.variants.handlers :as h]
            [short.products.handlers :as ph]
            [ring.util.response :as rr]
            [short.shared :as shared]))

(defn create-variant-controller! [database]
  (fn [request]
    (let [product-id (-> request :parameters :path :product-id shared/uuid-from-string)
          existing-product? (ph/get-product-by-uuid!
                             product-id
                             database)
          variant-data (-> request :parameters :body)]
      (if (empty? existing-product?)
        (rr/bad-request {:error "Something went wrong"})
        (rr/created ""
                    (h/create-variant!
                     (merge variant-data
                            {:product-id product-id})
                     database))))))
