(ns short.products.controllers
  (:require [short.products.handlers :as h]
            [ring.util.response :as rr]
            [short.products.views.details :as details]
            [short.render :as render]))

(defn create-product-controller! [database]
  (fn [request]
    (let [product-input (-> request :parameters :body)
          existing-product? (h/check-product-existence-by-slug!
                             product-input
                             database)]
      (if (empty? existing-product?)
        (rr/created ""
                    (h/create-product!
                     product-input
                     database))
        (rr/bad-request {:error "Something went wrong"})))))

(defn get-product-by-slug-controller! [database]
  (fn [request]
    (let [slug (-> request :parameters :path :product)
          product (h/get-product-by-slug! slug database)]
      (if (empty? product)
        (rr/not-found {:error "Product not found"})
        (rr/response product)))))

(defn render-test-controller [database]
  (fn [request]
    (let [slug (-> request :parameters :path :product)
          product (h/get-product-by-slug! slug database)
          view (details/render product)
          rendered (render/simplehtml-template view)]
      {:status 200 :body rendered})))
