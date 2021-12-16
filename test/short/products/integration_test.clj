(ns short.products.integration-test
  (:require [test-helpers :as th]
            [short.products.contracts :as c]
            [malli.core :as ml]
            [clojure.instant :as instant]
            [clojure.test :refer [deftest testing is]]))

(def product-body
  {:sku "fasjfsaoij123skulegal"
   :active true
   :slug "some-slug-sku-legal"
   :price 30
   :quantity 2})

(def ProductOut
  [:maybe
   [:map
    [:product/sku string?]
    [:product/active boolean?]
    [:product/slug string?]
    [:product/price number?]
    [:product/quantity number?]
    [:product/uuid uuid?]
    [:product/created_at inst?]]])

(deftest products-integration-creation-test
  (testing "Creates a product"
    (let [{:keys [status body]}
          (th/endpoint-test :post "/v1/products"
                            {:body product-body})
          parsed-res {:product/sku (:product/sku body)
                      :product/active (:product/active body)
                      :product/slug (:product/slug body)
                      :product/price (:product/price body)
                      :product/quantity (:product/quantity body)
                      :product/uuid (java.util.UUID/fromString
                                     (:product/uuid body))
                      :product/created_at (instant/read-instant-date
                                           (:product/created_at body))}]
      (is (= 201 status))
      (is (true? (ml/validate c/ProductOut parsed-res)))))
  (testing "Fails for an already registered slug"
    (let [_ (th/endpoint-test :post "v1/products"
                              {:body product-body})
          {:keys [status _body]}
          (th/endpoint-test :post "/v1/products"
                            {:body product-body})]
      (is (= 400 status)))))
